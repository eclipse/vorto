# Google Protobuf Generator

This generator converts an information model into Google protobuf and gRPC service files.

Example Functionblock

	namespace com.mycompany
	
	functionblock ColorLight {
	
		operations {
			setColor(color as Color) 
		}
	
	}
	
	entity Color {
		mandatory r as int
		mandatory g as int
		mandatory b as int
	}

Generated output

	package com.mycompany
	
	service NewFunctionBlock {
		rpc setColor(SetColorRequest) returns (SetColorResponse) {}
	
	}

	message SetColorRequest {
		Color color = 1;
	}

	message SetColorResponse {}

	message Color {
		int32 r = 1;
		int32 g = 2;
		int32 b = 3;
	}
	
Find more about compiling the output to different languages [here](https://grpc.io/docs/)

----------
List of other available [Code Generators](../Readme.md).