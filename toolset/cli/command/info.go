package command

/*
TODO: - converting value of 'createdOn' - long to date format

*/

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"strings"

	"github.com/eclipse/vorto/client/cli/config"
	"github.com/eclipse/vorto/client/cli/errorMessages"
	"github.com/eclipse/vorto/client/cli/models"
)

type InfoCommand struct {
	Namespace  string
	Client     *config.Client
	Repository string
	Name       string
	Version    string
}

func (c *InfoCommand) GetCommandName() string {
	return "info"
}

func NewInfoCommand(commandValue string, cfg *config.Configuration, client *config.Client) (*InfoCommand, error) {

	s := strings.Split(commandValue, ":")

	if len(s) < 2 {
		return nil, errorMessages.ERR_MALFORMED_NAMESPACE
	}

	namespace, version := s[0], s[1]
	x := strings.Split(namespace, ".")
	if len(x) < 2 {
		return nil, errorMessages.ERR_MALFORMED_NAMESPACE
	}

	name := x[len(x)-1]
	namespace = strings.Join(x[:len(x)-1], ".")

	return &InfoCommand{
		Namespace:  namespace,
		Client:     client,
		Version:    version,
		Name:       name,
		Repository: cfg.Repository,
	}, nil
}

func (this *InfoCommand) Execute() error {
	resp, err := this.Client.MyClient.Get(this.Repository + "/rest/model/" + this.Namespace + "/" + this.Name + "/" + this.Version)

	if err != nil {
		return errorMessages.ERR_NO_VALID_ID
	} else {
		defer resp.Body.Close()
		content, err := ioutil.ReadAll(resp.Body)
		if err != nil {
			return err
		}

		var model vorto.Model
		if err := json.Unmarshal(content, &model); err != nil {
			return errorMessages.ERR_MODEL_NOT_EXIST
		}

		//t := time.Unix(model.CreatedOn, 0)
		//		fmt.Println(t.Local())            // 2012-04-13 12:02:57 +0800 CST
		//		fmt.Println(t.Local().Unix()) // 1334289777
		//		fmt.Println(t.UTC()) // 2012-04-13 04:02:57 +0000 UTC
		//		fmt.Println(t.Unix()) // 1334289777

		//		tm := time.Unix(model.CreatedOn, 0)
		//		fmt.Println("Datum: ", tm)

		// Parse Outline
		fmt.Println()
		fmt.Println("Name		:", model.Id.Name)
		fmt.Println("Description 	:", model.Description)
		fmt.Println("Display Name 	:", model.DisplayName)
		fmt.Println("Namespace 	:", model.Id.Namespace)
		fmt.Println("Version 	:", model.Id.Version)
		fmt.Println("Created On 	:", model.CreatedOn.UTC())
		fmt.Println("Created By 	:", model.CreatedBy)

		fmt.Println("References:")
		for i := 0; i < len(model.References); i++ {
			fmt.Println("		 ", model.References[i].PrettyFormat)
		}

		fmt.Println("Used By:")
		for i := 0; i < len(model.ReferencedBy); i++ {
			fmt.Println("		 ", model.ReferencedBy[i].PrettyFormat)
		}
	}
	return nil
}
