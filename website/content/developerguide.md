---
title: "Developer Guide"
date: 2018-05-09T10:58:37+08:00
weight: 40
---

## Vorto Repository Plugin SDK

<table>
	<tr>
		<th>Code Generator Plugin</th>
		<td>Learn how to write your own generator and deploy and hook it into the Vorto Repository as a (micro) service</td>
		<td><a href="https://github.com/eclipse/vorto/blob/development/plugin-sdk/Readme.md">Read more</a></td>
	</tr>
	<tr>
		<th>Importer Plugin</th>
		<td>Importers convert device descriptions from other formats to the Vorto language. Learn how to write such an importer plugin and run and hook it as a (micro)service into the Vorto Repository.</td>
		<td><i>Coming soon</i></td>
	</tr>
</table>


## Repository Import API

If you want to manage other existing (standardized) device descriptions with the Vorto Repository, you can implement a custom importer using the [Importer API](https://github.com/eclipse/vorto/blob/development/repository/repository-importer/Readme.md).

## Device Payload Mapping API

Map arbitrary device payload, expressed as JSON or binary, to standardized data, that is described by Vorto Information Models. See [Getting started with Vorto Mappings](https://github.com/eclipse/vorto/blob/development/mapping-engine/Readme.md) for more information. 
