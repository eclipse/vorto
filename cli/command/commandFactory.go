package command

import (
	"os"

	. "github.com/eclipse/vorto/client/cli/config"
	"github.com/eclipse/vorto/client/cli/errorMessages"
)

type ICommand interface {
	GetCommandName() string
	Execute() error
}

type CommandFactory struct {
}

func NewCommandFactory() *CommandFactory {
	return &CommandFactory{}
}

func (f *CommandFactory) GetCommand(osArgs []string, cfg *Configuration, client *Client) (cmd ICommand, err error) {
	argsLen := len(os.Args)
	if argsLen < 2 {
		return nil, errorMessages.ERR_MISSING_ARGUMENTS
	}

	var commandText = os.Args[1]
	var commandValue string
	var commandArgs []string
	if argsLen > 2 {
		commandValue = os.Args[2]
		if argsLen > 3 {
			commandArgs = os.Args[3:]
		}
	}

	isHelp := false
	if (commandText == "-help" ||
		commandText == "-h" ||
		commandText == "help" ||
		commandText == "--help" ||
		commandText == "--h" ||
		commandText == "HELP" ||
		commandText == "-HELP") && argsLen == 2 {
		isHelp = true
	}

	switch {
	case isHelp:
		cmd, err = NewHelpCommand()
	case commandText == "config":
		commandArgs = os.Args[2:]
		cmd, err = NewConfigCommand(commandArgs, cfg, client)
	case commandText == "generators" && argsLen == 2:
		cmd, err = NewGeneratorsCommand(cfg, client)
	case commandText == "info" && commandValue != "":
		cmd, err = NewInfoCommand(commandValue, cfg, client)
	case commandText == "query" && commandValue != "":
		cmd, err = NewQueryCommand(commandValue, cfg, client)
	case commandText == "download" && commandValue != "":
		cmd, err = NewDownloadCommand(commandValue, commandArgs, cfg, client)
	case commandText == "generate" && commandValue != "":
		cmd, err = NewGenerateCommand(commandValue, commandArgs, cfg, client)
	case commandText == "share":
		cmd, err = NewShareCommand(commandValue, commandArgs, cfg, client)
	default:
		err = errorMessages.ERR_NO_VALID_COMMAND
	}

	return
}
