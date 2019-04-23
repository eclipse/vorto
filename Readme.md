[![Build Status](https://travis-ci.org/eclipse/vorto.svg?branch=development)](https://travis-ci.org/eclipse/vorto)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/bb310438f9684fd0b133eb8bebf14ee1)](https://www.codacy.com/app/alexander-edelmann/vorto?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=eclipse/vorto&amp;utm_campaign=Badge_Grade)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=org.eclipse.vorto%3Aparent&metric=alert_status)](https://sonarcloud.io/dashboard?id=org.eclipse.vorto%3Aparent)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.eclipse.vorto/parent/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.eclipse.vorto/parent)

[Eclipse Vorto](http://www.eclipse.org/vorto) allows device manufacturers to easily **describe device functionality** and characteristics as Device Information Models and **share & manage** them in a central [Vorto Repository](http://vorto.eclipse.org). Vorto provides convenient SDKs for IoT Device Developers to easily **integrate devices** into IoT solutions. 

The Bosch IoT Suite uses Vorto to define their entire device semantic abstraction layer and thus allows IoT Solution Developers to easily build device - agnostic IoT solutions. 

<img src="docs/images/vorto_cover.png"/>


## Getting Started with Vorto

[Read here](docs/gettingstarted.md)

## Vorto Examples

Check out a the [Vorto Example Code](https://www.github.com/eclipse/vorto-examples) that leverages Vorto Models and Vorto libraries.

## Repository Plugin SDK

The Repository Plugin SDK helps you build and deploy various extensions to the Vorto Repository:

<table>
	<tr>
		<th>Code Generator Plugin</th>
		<td>Learn how to write your own generator and deploy and hook it into the Vorto Repository as a (micro) service</td>
		<td><a href="plugin-sdk/Readme.md">Read more</a></td>
	</tr>
	<tr>
		<th>Importer Plugin</th>
		<td>Importers convert device descriptions from other formats to the Vorto language. Learn how to write such an importer plugin and run and hook it into the Vorto Repository.</td>
		<td><a href="repository/repository-importer/Readme.md">Read more</a></td>
	</tr>
</table>

## Documentation

- [Vorto DSL](core-bundles/docs/quickhelp_dsl.md)
- [Code Generators](generators/Readme.md)
- [Payload Mapping Engine](mapping-engine/Readme.md)
- [Tutorials](docs/gettingstarted.md)
- [Vorto DSL Utilities](utilities/Readme.md)

## Contact us
 - You want to chat with us ? [![Join the chat at https://gitter.im/eclipse/vorto](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/eclipse/vorto?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
 - You have problems with Vorto ? Open a [GitHub issue](https://github.com/eclipse/vorto/issues)
 - Find out more about the project on our [Vorto Homepage](http://www.eclipse.org/vorto)
 - Reach out to our developers on our [Discussion Forum](http://eclipse.org/forums/eclipse.vorto) 

## Follow us

| Channel | Releases           | Features                 | Tutorials                |
|:--------|:------------------:|:-------------------------|--------------------------|
| Twitter | :heavy_check_mark: | :heavy_check_mark:       | :heavy_check_mark:       |
| Email   | :heavy_check_mark: | :heavy_multiplication_x: | :heavy_multiplication_x: |

We favor Twitter for feature and tutorial news, because the nature of twitter allows for shorter and faster news. 
Also such a flood of information via Email would be considered spam. 
If you have suggestions on how and what news we should provide let us know in the issues.

## Contribute to the Project

When you create a Pull Request, make sure:

1. You have a valid CLA signed with Eclipse
2. All your commits are signed off (git commit -s)
3. Your commit message contains "Fixes #`<Github issue no>`
4. Target to merge your fix is development branch
5. Your code fits the code style guidelines


#### Attribution
The code style guidlines are taken from https://github.com/google/styleguide.



