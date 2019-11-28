# [Mapping Performance - JUnit Perf Load Testing](https://wiki.bosch-si.com/display/EV/Mapping+Performance+-+JUnit+Perf+Load+Testing)


# Challenge

-   The general performance and behavior of the mapping engine under high payload are unknown.

## Idea

-   Implementation of load tests to measure the performance according to different mapping scenarios.
-   Generation of statistical reports about key performance metrics as e.g average execution time or min, max latency.

## Solution

-   Implementation of benchmark/performance test scenarios using [JUnitPerf](https://github.com/noconnor/JUnitPerf)  annotations in usage of existing mapping engine test functions.
-   Integration of load tests in the Travis build process to provide direct feedback through an exported html report, accessible via web-browser.  
      
    

## **Performance Testing**

### **Environment:**

**Processor:**  Intel(R) Core(TM) i7-8650U CPU @ 1.9GHz 2.11 Ghz

**Installed memory (RAM):** 32GB

**System:**  64-bit Oberating System, x64-based processor, Windows 10 Enterprise

### **Configuration Parameters:**

The selected [JUnitPerf](https://github.com/noconnor/JUnitPerf) framework allows to annotate test functions with custom java annotations to run the selected tests multiple times while gathering statistical information. The annotation syntax allows the configuration of the following parameters.

**threads -** The total number of threads to use during test execution

**warmUpMs -** The warm up period in ms, test logic will be executed during warm up, but results will not be considered during statistics evaluation  

**durationMs -** The total time to run the test in millisecs (ms) (includes warmup period)

**maxExecutionsPerSecond -** The maximum number of iteration per second

**rampUpPeriodMs -** The framework ramps up its executions per second smoothly over the duration of this period


## **Scenario 1 - Binary Data Mapping**

For the reason that the normalization of binary data into a mapped output requires conversion, the Binary Data Mapping tests described in the following only imply cases that make use of a converter.

****With Built-In Converter:****
-  **Test-Case 1:** 1 Built-in (Java) Converter Function

****With JavaScript Converter:****
-   **Test-Case 2:** Two binary data points, mapping with  Double Nested Converter Functions (Built-in (Java) + Javascript)
-   **Test-Case 3:**  Triple Nested Converter Functions (Built-in + Built-in + Javascript)

**Each of the tests is executed 4 times with different configuration parameters:**

<table style="width:100%">  
<tr>  
<th>Execution</th>  
<th>threads</th>  
<th>warmUpMs</th>  
<th>durationMs</th>  
<th>maxExecutionsPerSecond</th>  
<th>rampUpPeriodMs</th>  
<th>Description</th>  
</tr> 
<tr>  
<td>1</td>  
<td>1</td>  
<td>10_000</td>  
<td>15_000</td>  
<td>2_000</td>  
<td>2_000</td>  
<td>A single thread and a an amount of max 2000  executions/s over 15 seconds</th>  
</tr>  
<tr>  
<td>2</td>  
<td>5</td>  
<td>10_000</td>  
<td>50_000</td>  
<td>1_000</td>  
<td>2_000</td>  
<td>Five threads and max of 1000 executions/s over 50 seconds.</td>  
</tr>  
<tr>  
<td>3</td>
<td>10</td>  
<td>10_000</td>  
<td>150_000</td>  
<td>1_000</td>  
<td>2_000</td>  
<td>Ten threads and max of 1000 executions/s over 150 seconds.</td>  
</tr> 
<tr>  
<td>4</td>
<td>15</td>  
<td>10_000</td>  
<td>300_000</td>  
<td>1_000</td>  
<td>2_000</td>  
<td>Fifteen threads and max of 1000 executions/s over 300 seconds.</td>  
</tr>   
</table>

## **Result -** **Scenario 1** **:** Binary

#### A complete overview of the results can be found under this link [benchmark_binary.html](empty_link.html)

## Test Case 1
<table style="width:100%">  
<tr>  
<th>
<p> 1 Built-in (Java) Converter Function</p>
</th>  
<th>Input</th>  
<th>Expected Output:</th>  
<th>Threads</th> 
<th>Execution Time</th>
<th>Throughput</th>
<th>Min Latency</th>
<th>Max Latency</th>
<th>Average Latency</th>
<th>Invocations</th>
<th>Errors</th>
</tr>  
<tr>  
<td>Execution 1</td>  
 <td rowspan="4"> {"data" : "MjA="}</td>
 <td rowspan="4">digital_input_state = 20</td>
 <td>1</td>  
<td>15.000 ms</td>  
<td>1,192 / s</td>  
<td>0.01 ms</td>  
<td>0.15 ms</td>  
<td>0.02 ms</td>  
<td>5,964</td>  
<td>0</td>  
</tr>  
<tr>  
<td>Execution 2</td>  
 <td>5</td>  
<td>50.000 ms</td>  
<td>1,000/ s</td>  
<td>0.02 ms</td>  
<td>0.69 ms</td>  
<td>0.04 ms</td>  
<td>40.001</td>  
<td>0</td>  
</tr>  
<tr>  
<td>Execution 3</td>  
 <td>10</td>  
<td>150.000 ms</td>  
<td>1,000/ s</td>  
<td>0.01 ms</td>  
<td>13,21 ms</td>  
<td>0,07 ms</td>  
<td>140,000 </td>  
<td>0</td>  
</tr>  
<tr>  
<td>Execution 4</td>  
 <td>15</td>  
<td>300.000 ms</td>  
<td>1,000/ s</td>  
<td>0.01 ms</td>  
<td>6,31 ms</td>  
<td>0,06 ms</td>  
<td>290,002 </td>  
<td>0</td>  
</tr>  
</table>


## Test Case 2
<table style="width:100%">  
<tr>  
<th>Two data points, double Nested Converter Functions (Built-in (Java) + Javascript)
</th>  
<th>Input</th>  
<th>Expected Output:</th>  
<th>Threads</th> 
<th>Execution Time</th>
<th>Throughput</th>
<th>Min Latency</th>
<th>Max Latency</th>
<th>Average Latency</th>
<th>Invocations</th>
<th>Errors</th>
</tr>  
<tr>  
<td>Execution 1</td>  
 <td rowspan="4">Binary Data with 2 bytes temperature (Byte 1-2), 2 bytes humidity (Byte 3-4)</td>
 <td rowspan="4">temperature = 20
humidity = 88.19</td>
 <td>1</td>  
<td>15.000 ms</td>  
<td>327/ s</td>  
<td>2,45 ms</td>  
<td>178,96 ms</td>  
<td>3,05 ms</td>  
<td>1,638 </td>  
<td>0</td>  
</tr>  
<tr>  
<td>Execution 2</td>  
 <td>5</td>  
<td>50.000 ms</td>  
<td>609/ s</td>  
<td>3,00 ms</td>  
<td>321,31 ms</td>  
<td>7,59 ms</td>  
<td>24,363</td>  
<td>0</td>  
</tr>  
<tr>  
<td>Execution 3</td>  
 <td>10</td>  
<td>150.000 ms</td>  
<td>604/ s</td>  
<td>2,66 ms</td>  
<td>387,69 ms</td>  
<td>15,01 ms</td>  
<td>84,639 </td>  
<td>0</td>  
</tr>  
<tr>  
<td>Execution 4</td>  
 <td>15</td>  
<td>300.000 ms</td>  
<td>505/ s</td>  
<td>2,42 ms</td>  
<td>474,81 ms</td>  
<td>26,91 ms</td>  
<td>146,481 </td>  
<td>0</td>  
</tr>  
</table>

## Test Case 3
<table style="width:100%">  
<tr>  
<th>Double Nested Converter Functions (Built-in (Java) + Javascript)
</th>  
<th>Input</th>  
<th>Expected Output:</th>  
<th>Threads</th> 
<th>Execution Time</th>
<th>Throughput</th>
<th>Min Latency</th>
<th>Max Latency</th>
<th>Average Latency</th>
<th>Invocations</th>
<th>Errors</th>
</tr>  
<tr>  
<td>Execution 1</td>  
 <td rowspan="4">{"data" : "4f00630063007500700061006e0063007900200002"}</td>
 <td rowspan="4">sensor_value = 2</td>
 <td>1</td>  
 <td>15.000 ms</td>  
<td>512 ms</td>  
<td>0,72 / s</td>  
<td>159,68 ms</td>  
<td>1,46 ms</td>  
<td>2,561</td>  
<td>0</td>  
</tr>  
<tr>  
<td>Execution 2</td>  
 <td>5</td>  
<td>50.000 ms</td>  
<td>918 / s</td>  
<td>0,69 ms</td>  
<td>235,15 ms</td>  
<td>1,96 ms</td>  
<td>36,735</td>  
<td>0</td>  
</tr>  
<tr>  
<td>Execution 3</td>  
 <td>10</td>  
<td>150.000 ms</td>  
<td>931/ s</td>  
<td>0.89</td>  
<td>317,7 s</td>  
<td>2,26 ms</td>  
<td>130,426</td>  
<td>0</td>  
</tr>  
<tr>  
<td>Execution 4</td>  
 <td>15</td>  
<td>300.000 ms</td>  
<td>924 /s</td>  
<td>1,15 ms</td>  
<td>386,41 ms</td>  
<td>3,16 ms</td>  
<td>268,218 </td>  
<td>0</td>  
</tr>  
</table>

## **Scenario 2 - Json Data Mapping**

****Without Converter:****

-   **Test-Case 1:** Simple mapping of temperature value in Json to outdoorTemperature

**With JavaScript Converter:**

-   **Test-Case 2:** Mapping and conversion of Json with multiple custom JS functions

**With Built-in Converter:**

-   **Test-Case 3:** Mapping and conversion of timestamp in Json into date type sensor value

**Each of the tests is executed 4 times with different configuration parameters:**

<table style="width:100%">  
<tr>  
<th>Execution</th>  
<th>threads</th>  
<th>warmUpMs</th>  
<th>durationMs</th>  
<th>maxExecutionsPerSecond</th>  
<th>rampUpPeriodMs</th>  
<th>Description</th>  
</tr> 
<tr>  
<td>1</td>  
<td>1</td>  
<td>10_000</td>  
<td>15_000</td>  
<td>10_000</td>  
<td>2_000</td>  
<td>A single thread and a an amount of max 10.000 executions/s over 15 seconds.</th>  
</tr>  
<tr>  
<td>2</td>  
<td>5</td>  
<td>10_000</td>  
<td>50_000</td>  
<td>10_000</td>  
<td>2_000</td>  
<td>Five threads and max of 10.000 executions/s over 50 seconds.</td>  
</tr>  
<tr>  
<td>3</td>
<td>10</td>  
<td>10_000</td>  
<td>150_000</td>  
<td>10_000</td>  
<td>2_000</td>  
<td>Ten threads and max of 10.000 executions/s over 150 seconds.</td>  
</tr> 
<tr>  
<td>4</td>
<td>20</td>  
<td>10_000</td>  
<td>300_000</td>  
<td>5_000</td>  
<td>2_000</td>  
<td>Twenty threads and max of 5.000 executions/s over 300 seconds.</td>  
</tr>   
</table>



## **Result - Scenario 2:**
An overview of the complete results can be found under this link [benchmark_json.html](empty_url.html)

## Test Case 1
<table style="width:100%">  
<tr>  
<th>
<p> No converter functions</p>
</th>  
<th>Input</th>  
<th>Expected Output:</th>  
<th>Threads</th> 
<th>Execution Time</th>
<th>Throughput</th>
<th>Min Latency</th>
<th>Max Latency</th>
<th>Average Latency</th>
<th>Invocations</th>
<th>Errors</th>
</tr>  
<tr>  
<td>Execution 1</td>  
 <td rowspan="4"> {"temperature" : 20.3 }</td>
 <td rowspan="4">outdoorTemperature = 20.3</td>
 <td>1</td>  
<td>15.000 ms</td>  
<td>1,185 / s</td>  
<td>0,00 ms</td>  
<td>0.13 ms</td>  
<td>0.02 ms</td>  
<td>5926</td>  
<td>0</td>  
</tr>  
<tr>  
<td>Execution 2</td>  
 <td>5</td>  
<td>50.000 ms</td>  
<td>3,619 / s</td>  
<td>0,00 ms</td>  
<td>3.84 ms</td>  
<td>0.01 ms</td>  
<td>144,794</td>  
<td>0</td>  
</tr>  
<tr>  
<td>Execution 3</td>  
 <td>10</td>  
<td>150.000 ms</td>  
<td>4,999/ s</td>  
<td>0,00 ms</td>  
<td>4,96 ms</td>  
<td>0,02 ms</td>  
<td>699,937</td>  
<td>0</td>  
</tr>  
<tr>  
<td>Execution 4</td>  
 <td>20</td>  
<td>300.000 ms</td>  
<td>4,999/ s</td>  
<td>0,00 ms</td>  
<td>9,29 ms</td>  
<td>0,01 ms</td>  
<td>1,449,991</td>  
<td>0</td>  
</tr>  
</table>


## Test Case 2
<table style="width:100%">  
<tr>  
<th>
<p>1 Built-in Converter Function</p>
</th>  
<th>Input</th>  
<th>Expected Output:</th>  
<th>Threads</th> 
<th>Execution Time</th>
<th>Throughput</th>
<th>Min Latency</th>
<th>Max Latency</th>
<th>Average Latency</th>
<th>Invocations</th>
<th>Errors</th>
</tr>  
<tr>  
<td>Execution 1</td>  
 <td rowspan="4"> {"time" : 1574736601479}</td>
 <td rowspan="4">sensor_value = 2019-01-01 15:33:33 +0800</td>
 <td>1</td>  
<td>15.000 ms</td>  
<td>1,196 / s</td>  
<td>0,01 ms</td>  
<td>0.32 ms</td>  
<td>0.05 ms</td>  
<td>5,983</td>  
<td>0</td>  
</tr>  
<tr>  
<td>Execution 2</td>  
 <td>5</td>  
<td>50.000 ms</td>  
<td>3,527 / s</td>  
<td>0,01 ms</td>  
<td>42,92 ms</td>  
<td>0.06 ms</td>  
<td>141,116</td>  
<td>0</td>  
</tr>  
<tr>  
<td>Execution 3</td>  
 <td>10</td>  
<td>150.000 ms</td>  
<td>4,994 / s</td>  
<td>0,01 ms</td>  
<td>11,63 ms</td>  
<td>0,06 ms</td>  
<td>699,284</td>  
<td>0</td>  
</tr>  
<tr>  
<td>Execution 4</td>  
 <td>20</td>  
<td>300.000 ms</td>  
<td>4,996/ s</td>  
<td>0,01 ms</td>  
<td>21,80 ms</td>  
<td>0,06 ms</td>  
<td>1,448,878</td>  
<td>0</td>  
</tr>  
</table>

## Test Case 3
<table style="width:100%">  
<tr>  
<th>
<p>1 Built-in Converter Function</p>
</th>  
<th>Input</th>  
<th>Expected Output:</th>  
<th>Threads</th> 
<th>Execution Time</th>
<th>Throughput</th>
<th>Min Latency</th>
<th>Max Latency</th>
<th>Average Latency</th>
<th>Invocations</th>
<th>Errors</th>
</tr>  
<tr>  
<td>Execution 1</td>  
 <td rowspan="4"> "{\"clickType\" : \"DOUBLE\"}"</td>
 <td rowspan="4">{"button":{"digital_input_count":2,"digital_input_state":true}}</td>
 <td>1</td>  
<td>15.000 ms</td>  
<td>493 / s</td>  
<td>1,32 ms</td>  
<td>79,60 ms</td>  
<td>1,99 ms</td>  
<td>2,469</td>  
<td>0</td>  
</tr>  
<tr>  
<td>Execution 2</td>  
 <td>5</td>  
<td>50.000 ms</td>  
<td>1,290 / s</td>  
<td>1,20 ms</td>  
<td>253,16 ms</td>  
<td>2,99 ms</td>  
<td>51,637</td>  
<td>0</td>  
</tr>  
<tr>  
<td>Execution 3</td>  
 <td>10</td>  
<td>150.000 ms</td>  
<td>1,396/ s</td>  
<td>1,46 ms</td>  
<td>328,80 ms</td>  
<td>6,06 ms</td>  
<td>195,528</td>  
<td>0</td>  
</tr>  
<tr>  
<td>Execution 4</td>  
 <td>20</td>  
<td>300.000 ms</td>  
<td>1,154/ s</td>  
<td>1,58 ms</td>  
<td>461,84 ms</td>  
<td>15,7 ms</td>  
<td>334,853</td>  
<td>0</td>  
</tr>  
</table>