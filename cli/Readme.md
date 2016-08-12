# Vorto CLI - Developer setup


##Prerequisites:
Download and install, 

- [Go](https://golang.org/dl/) 


##Configure
After **Go** is installed on your system for respective OS, configure your system with the following steps,
1. Set environment variable `GOROOT` to path where Go is installed.

		Windows:   set GOROOT=C:\Software\Go
		Linux/Mac: export GOROOT=/home/username/Go

2. Set environment variable `PATH`  to execute go commands in commandline.
 
	 	Windows:   set PATH=%PATH%;C:\Software\Go\bin
		Linux/Mac: export PATH=$PATH:/home/username/Go/bin
3. Set environment variable `GOPATH` to path where you want to setup Go workspace. 
	 	

		Windows:   set GOPATH=C:\Software\Go\workspace
		Linux/Mac: export GOPATH=/home/username/Go/workspace
4. Create source directories within your workspace as 

		Windows:   cd C:\Software\Go\workspace 
   				   mkdir -p src\github.com\eclipse\vorto\client
		Linux/Mac: cd /home/username/Go/workspace
				   mkdir -p src/github.com/eclipse/vorto/client

5. git clone Eclipse Vorto into the Go work directory.
		Windows:   cd C:\Software\Go\workspace\src\github.com\eclipse\vorto\client
				   git clone https://github.com/eclipse/vorto.git
		Linux/Mac: cd /home/username/Go/workspace/src/github.com/eclipse/vorto/client
				   git clone https://github.com/eclipse/vorto.git 
6. git clone **go-gypsy** for Vorto CLI tool dependencies
		Windows:   cd C:\Software\Go\workspace\src\github.com				   				  
				   git clone https://github.com/kylelemons/go-gypsy.git kylelemons/go-gypsy	    
		Linux/Mac: cd /home/username/Go/workspace/src/github.com/
				   git clone https://github.com/kylelemons/go-gypsy.git kylelemons/go-gypsy	

##Build

After the workspace is configured, build Vorto CLI tool with the following instructions.

1. Build Vorto Cli tool
		Windows:   cd C:\Software\Go\workspace\src\github.com\eclipse\vorto\client\cli
				   go build -o vorto.exe vorto.go
		Linux/Mac: cd /home/username/Go/workspace/src/github.com/eclipse/vorto/client/cli
				   go build -o vorto vorto.go


##Run
1. Run Vorto Cli tool,
		Windows:   cd C:\Software\Go\workspace\src\github.com\eclipse\vorto\client\cli
				   vorto.exe help 
		Linux/Mac: cd /home/username/Go/workspace/src/github.com/eclipse/vorto/client/cli
				   ./vorto help

For Vorto CLI commands reference documentation refer, [Vorto CLI Reference.](http://www.eclipse.org/vorto/documentation/vorto-repository/cli-tool/cli-tool.html) 