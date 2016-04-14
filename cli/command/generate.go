package command

import (
	"flag"
	"fmt"
	"io"
	"os"
	"path/filepath"
	"strings"

	"github.com/eclipse/vorto/client/cli/config"
	"github.com/eclipse/vorto/client/cli/errorMessages"
)

type GenerateCommand struct {
	Namespace  string
	Name       string
	Version    string
	Generator  string
	OutputPath string
	Client     *config.Client
	Repository string
}

func (c *GenerateCommand) GetCommandName() string {
	return "generate"
}

func NewGenerateCommand(commandValue string, commandArgs []string, cfg *config.Configuration, client *config.Client) (*GenerateCommand, error) {

	s := strings.Split(commandValue, ":")

	if len(s) < 2 {
		return nil, errorMessages.ERR_MALFORMED_COMMAND
	}

	namespace, version := s[0], s[1]
	x := strings.Split(namespace, ".")
	if len(x) < 2 {
		return nil, errorMessages.ERR_MALFORMED_NAMESPACE
	}

	name := x[len(x)-1]
	namespace = strings.Join(x[:len(x)-1], ".")

	flg := flag.NewFlagSet("generate context", flag.ContinueOnError)
	generator := flg.String("generatorKey", "", "specifies platform generator")
	outputPath := flg.String("outputPath", "", "outputPath specifics download directory")

	if err := flg.Parse(commandArgs); err == nil {

		g := *generator
		o := *outputPath

		if o == "" {
			dir, err := filepath.Abs(filepath.Dir(os.Args[0]))
			if err != nil {
				println(err)
			}
			o = dir
		}

		return &GenerateCommand{
			Generator:  g,
			OutputPath: o,
			Namespace:  namespace,
			Name:       name,
			Version:    version,
			Client:     client,
			Repository: cfg.Repository,
		}, nil
	}
	return nil, errorMessages.ERR_NO_DEFAULT_GENERATE_COMMAND
}

func (this *GenerateCommand) Execute() error {

	resp, err := this.Client.MyClient.Get(this.Repository + "/rest/generation-router/" + this.Namespace + "/" + this.Name + "/" + this.Version + "/" + this.Generator)

	if err != nil {
		return err
	} else {

		defer resp.Body.Close()
		if resp.StatusCode == 200 {
			// check directory spelling
			if !strings.HasSuffix(this.OutputPath, "\\") {
				this.OutputPath = this.OutputPath + "\\"
			}

			exists(this.OutputPath)
			file, err := os.Create(this.OutputPath + this.Name + ".zip")

			if err != nil {
				return err
			}
			defer file.Close()

			size, err := io.Copy(file, resp.Body)

			if err != nil {
				return err
			}

			println()
			fmt.Printf("Message: "+this.Generator+" generation successful, %swith %v bytes", this.OutputPath+this.Name+".zip ", size)
			println()
		} else {
			return errorMessages.ERR_GENERATION_ONLY_INFOMODEL
		}
	}

	return nil
}
