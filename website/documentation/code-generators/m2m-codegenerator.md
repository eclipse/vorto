---
layout: documentation
title: Example - Bosch M2M Platform Code Generator
---
{% include base.html %}
## Example Code Generator - Bosch M2M Platform

A function block model contains necessary artifacts (e.g., Java representation of the model) which can be used by the M2M Agent Hub and the Central Registry at run time.

**Prerequisites**  

- You have created and edited a function block project (refer to [Creating a New Function Block Project]({{base}}/documentation/editors/functionblock.html#creating-a-new-function-block) and [Editing a Function Block]({{base}}/documentation/editors/functionblock.html#editing-a-function-block)).  
- You have selected the Vorto perspective.

<table class="table table-bordered">
  <tbody>
    <td><i class="fa fa-info-circle info-note"></i></td>
    <td>Currently, there is the following limitation:<br>
    Although you can drop multiple function blocks in an information model, the Bosch M2M code generator considers only one function block model in an information model (namely the first).</td>
  </tr></tbody>
</table>

**Proceed as follows**

1. In the **Information Models** browser, select the function block project you want to generate code for.
2. Right-click to open the context menu and click **Generate Code > Bosch M2M Model Generator**.  
   ![Generating code]({{base}}/img/documentation/m2m_tc_generate_code_function_block_model_1.png)  
   The following is generated and Eclipse switches to the Java perspective:
   - The M2M model project (`<your_function_block>-model`).
   - The M2M service project (`<your_function_block>-service`).
   - The dummy base driver project (`dummy-basedriver`).  

<table class="table table-bordered">
  <tbody><tr>
    <td><i class="fa fa-info-circle info-note"></i></td>
    <td>There might be some errors on the generated project. You can select the project, then right-click to open the context menu and click <b>Maven-Update Project...</b> to resolve the errors.</td>
  </tr></tbody>
</table>

<table class="table table-bordered">
  <tbody><tr>
    <td><i class="fa fa-info-circle info-note"></i></td>
    <td>Please note that the default generated function block service uses the latest M2M API that exist in maven repository for dependency. If your target platform is not the latest (e.g., latest is 2.2.5 but your target platform is running 2.2.0), then you need to update the property <code>m2m.version</code> in the <code>pom.xml</code> file in your service project to your actual M2M version. Then update your project. You can check the actual M2M version used by expanding maven dependencies from your service project.</td>
  </tr></tbody>
</table>
