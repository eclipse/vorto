package command

/*
TODO - REST Search doesn't work correct but this dependence on server side
*/

import (
	"encoding/json"
	"fmt"
	"io/ioutil"

	"github.com/eclipse/vorto/client/cli/config"
	"github.com/eclipse/vorto/client/cli/errorMessages"
	"github.com/eclipse/vorto/client/cli/models"
)

type QueryCommand struct {
	Expression string
	Client     *config.Client
	Repository string
}

func (c *QueryCommand) GetCommandName() string {
	return "query"
}

func NewQueryCommand(commandValue string, cfg *config.Configuration, client *config.Client) (*QueryCommand, error) {

	return &QueryCommand{
		Expression: commandValue,
		Client:     client,
		Repository: cfg.Repository,
	}, nil
}

func (this *QueryCommand) Execute() error {

	resp, err := this.Client.MyClient.Get(this.Repository + "/rest/model/query=" + this.Expression)
	if err != nil {
		return errorMessages.ERR_REPO
	} else {
		defer resp.Body.Close()
		contents, err := ioutil.ReadAll(resp.Body)
		if err != nil {
			return errorMessages.ERR_REPO
		}

		var models []vorto.Model
		if err := json.Unmarshal(contents, &models); err != nil {
			return err
		}

		println()
		fmt.Println("Query result: ", len(models))
		for i := 0; i < len(models); i++ {
			fmt.Println(models[i].Id.PrettyFormat)
		}
	}

	return nil
}
