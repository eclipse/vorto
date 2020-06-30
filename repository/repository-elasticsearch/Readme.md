# Elasticsearch service

## Abstract

Vorto relies on [Elasticsearch](https://www.elastic.co/products/elasticsearch) to search for models 
in productive environments.

Conversely, a custom JCR query building mechanism is in use in development environments. 

Both resulting search services use a common intermediary syntax to simplify and harmonize the search. 

The search is available as a text field in the [web UI](https://www.eclipse.org/vorto), and can be 
also performed as a GET request in the [HTTP API](https://www.eclipse.org/vorto/swagger/#/Models/SearchForModels).  
 
## How to search

### Syntax
Vorto model can be searched with the following syntax.

See the [vortolang 1.0 specification](https://github.com/eclipse/vorto/blob/master/docs/vortolang-1.0.md) 
for a full description of all properties below.   

Tag | Description | Values |Tag required | Example
------|-------------|--------|--------------|--------
Name | The model's name. <ul><li>If the term is tagged explicitly with `name:`, will search the `displayName` field</li><li>If the term is not tagged, will search either the `displayName` or the `description` fields</li></ul>Values with no wildcards will automatically be added a trailing multi-character wildcard, e.g. `name:abc` becomes `name:abc*` behind the scenes.  | Any | No |  <ul><li>`name:RaspberryPi`</li><li>`RaspberryPi`</li><li>`Rasp*`</li><li>`*aspberry??`</li><li>`name:*Pi`</li></ul>
Author | The author of the model | Any | Yes | <ul><li>`author:mena`</li><li>`author:m*`</li></ul>
User reference | A shortcut for either `author:` or `lastModifiedBy` | Any | Yes | <ul><li>`userReference:m*`</li></ul>
Visibility | Whether the model is private or public | <ul><li>`Private`</li><li>`Public`</li></ul> | Yes | <ul><li>`visibility:Private`</li><li>`visibility:*`</li></ul>
Type | The model's type | <ul><li>`Functionblock`</li><li>`InformationModel`</li><li>`Datatype`</li><li>`Mapping`</li></ul> | Yes | <ul><li>`type:InformationModel`</li><li>`type:funct*`</li></ul>
State | The model's state | <ul><li>`Draft`</li><li>`InReview`</li><li>`Released`</li><li>`Deprecated`</li></ul> | Yes | <ul><li>`state:Draft`</li><li>`state:release?`</li></ul>
Namespace | The model's namespace | Any | Yes | <ul><li>`namespace:vorto.private.mynamespace`</li><li>`namespace:org.*`</li></ul>
Version | The model's version | Any | Yes | <ul><li>`version:1.*`</li><li>`version:?.?.1`</ul>

**Note**: The [web UI](https://www.eclipse.org/vorto) contains a few drop-downs and checkboxes to 
complement the text-based search - those elements are equivalent to specifying a fixed value for a tag. 

### Search logic
Combined together, these search terms can produce powerful searches, with the following rules:

* All tags and values are **case-insensitive**
* Values cannot contain whitespace
* Tagged expressions must follow the strict `[tag name case-insensitive][:][value]` syntax
* All values can contain any number of single character (`?`) or multi-character (`*`) wildcards, 
even enumerated values like types or states 
* Tags themselves **cannot** contain wildcards
* Multiple tags can be used, with the following rule:
   * All identical tags e.g. `name:a* name:*b` will return models matching **either** expression, 
   inclusively
   * All different tags (including un-tagged `name` searches) will return models matching **all** 
   expressions
   
### Advanced search examples

The following examples illustrate and elaborate on the rules above.

Description | Syntax | Explanation
------------|--------|------------
Multi-tag search with same tags | `mymodel* name:my*2` | <ul><li>The first term is an implicit `name:` search</li><li>The second term is an explicit `name:` search.</li><li>This expression will return models whose name or description **either** starts with `mymodel`, **or** ends starts with `my` and ends with `2`
Multi-tag search with different tags | `name:my* type:info* state:rel*` | This will search for models that fulfill **all** conditions listed below:<ul><li>The model's name starts with `my`</li><li>The model's type starts with `info` (i.e. only models of type `InformationModel`)</li><li>The model's state starts with `rel` (i.e. only `Released` models)</li></ul>Models that do not fulfill all of those conditions altogether will not be returned.
Complex search | `mymodel* myothermodel* userReference:bob visibility:* type:function* type:mapping state:draft state:\*re* namesapce:com.mycompany* version:1.0.?`| This will return all models that fulfill all of the following conditions:<ul><li>The model's name starts with either `mymodel` or `myothermodel`</li><li>The model's author or last modifying user is `Bob`</li><li>Model visibility is irrelevant (thus this term is redundant here)</li><li>Model's type is either `Functionblock` or `Mapping`</li><li>Model's state is either `Draft`, or `InReview` **or** `Released` (note the `*` wildcard is for `0` or more characters)</li><li>The model's version starts with `1.0.` and has exactly *one* character after (note that the `?` wildcard would also allow `1.0.` as a version, but that would be an invalid version according to specifications, so no chance of finding that)</li></ul> 

## Index changes requiring re-creating the index

At time, the Elasticsearch index can change, e.g. when a model index field type is modified. 

This can happen when allowing free-text search for enumerated types, thus changing the index type 
from `key` to `text`. 

This requires re-creating the whole Vorto index for Elasticsearch.

One straightforward way to do this is to send an HTTP POST request to the `/rest/forcereindex` 
endpoint while authorized as a system administrator (i.e. with role `sysadmin`). 

In the web UI, this is done by:
 
1. Logging in to the Vorto site as a system administrator
2. Navigating to the **manage** section
3. Selecting the **Global repository functions** tab
4. Clicking **Re-create index** (as opposed to just **Reindex**) 

The operation will entirely re-create the Vorto index, then re-index the model. 

**Caution is advised: back-up the model first.**

  