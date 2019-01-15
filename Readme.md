[![Build Status](https://travis-ci.org/eclipse/vorto.svg?branch=development)](https://travis-ci.org/eclipse/vorto)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/569649bfe2594bedae2cd172e5ee0741)](https://www.codacy.com/app/alexander-edelmann/vorto?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=eclipse/vorto&amp;utm_campaign=Badge_Grade)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=org.eclipse.vorto%3Aparent&metric=alert_status)](https://sonarcloud.io/dashboard?id=org.eclipse.vorto%3Aparent)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.eclipse.vorto/parent/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.eclipse.vorto/parent)

[Eclipse Vorto](http://www.eclipse.org/vorto) allows device manufacturers to easily **describe device functionality** and characteristics as Device Information Models and **share & manage** them in a central [Vorto Repository](http://vorto.eclipse.org). Vorto provides convenient tools for IoT Device Developers to convert Device Information Models to source code that runs on the device or gateway, easing the **integration with IoT Platforms**.

<img src="docs/images/vorto_cover.png"/>


## Getting Started with Vorto

Take a look at the following demo showcasing how Vorto Models are used to simplify the integration of devices with the Bosch IoT Suite Platform: [Click here](https://vorto.eclipse.org/console) for the Demo.

## Repository Plugin SDK

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



