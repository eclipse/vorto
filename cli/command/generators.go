package command

import (
	"encoding/json"
	"io/ioutil"
	"strconv"

	"github.com/eclipse/vorto/client/cli/config"
	"github.com/eclipse/vorto/client/cli/errorMessages"
	"github.com/eclipse/vorto/client/cli/models"
)

type GeneratorsCommand struct {
	Client     *config.Client
	Repository string
}

func (c *GeneratorsCommand) GetCommandName() string {
	return "generators"
}

func NewGeneratorsCommand(cfg *config.Configuration, client *config.Client) (*GeneratorsCommand, error) {

	return &GeneratorsCommand{
		Client:     client,
		Repository: cfg.Repository,
	}, nil
}

func (this *GeneratorsCommand) Execute() error {

	resp, err := this.Client.MyClient.Get(this.Repository + "/rest/generation-router/platform")

	if err != nil {
		return err
	} else {
		defer resp.Body.Close()
		contents, err := ioutil.ReadAll(resp.Body)
		if err != nil {
			return err
		}

		var generator []vorto.Generator
		if err := json.Unmarshal(contents, &generator); err != nil {
			return errorMessages.ERR_NO_ACTIVE_GENERATORS
		}
		println()
		// Parse Outline
		for i := 0; i < len(generator); i++ {

			println(strconv.Itoa(i+1) + "). Platform -	" + generator[i].Name)
			println()

			println("  Key: 	  	  " + generator[i].Key)
			println("  Name: 	  " + generator[i].Name)
			print("  Rating: 	  ")

			if generator[i].Rating == "NONE" {
				print("	 (" + generator[i].Rating + ") \n")
			} else if generator[i].Rating == "FAIR" {
				print("*	(" + generator[i].Rating + ") \n")
			} else if generator[i].Rating == "GOOD" {
				print("**	(" + generator[i].Rating + ") \n")
			} else if generator[i].Rating == "VERY GOOD" {
				print("***		(" + generator[i].Rating + ") \n")
			} else if generator[i].Rating == "EXCELLENT" {
				print("****		(" + generator[i].Rating + ") \n")
			}

			println("  Description:	  " + generator[i].Description)
			println("  Creator: 	  " + generator[i].Creator)
			println("  Documentation:  " + generator[i].DocumentationUrl)
			println()
		}
		println()
	}

	return nil
}
