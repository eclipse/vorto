package main

import (
	"fmt"
	"os"

	"github.com/eclipse/vorto/client/cli/command"
	"github.com/eclipse/vorto/client/cli/config"
)

/*
 TODO:
*/

func main() {

	//	Configuration
	configuration := config.NewConfiguration()
	client := config.NewClient(configuration)

	commandFactory := command.NewCommandFactory()
	command, err := commandFactory.GetCommand(os.Args, configuration, client)

	if err != nil {
		fmt.Print(err)
		return
	}

	err = command.Execute()
	if err != nil {
		fmt.Print(err)
	}
}
