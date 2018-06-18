package command

/*
TODO: - converting value of 'createdOn' - long to date format

*/

import (
	"flag"
	"io"
	"os"
	"os/user"
	"runtime"
	"strings"

	"github.com/eclipse/vorto/client/cli/config"
	"github.com/eclipse/vorto/client/cli/errorMessages"
)

type ConfigCommand struct {
	Proxy      string
	Repository string
}

func (c *ConfigCommand) GetCommandName() string {
	return "config"
}

func NewConfigCommand(commandArgs []string, cfg *config.Configuration, client *config.Client) (*ConfigCommand, error) {

	flg := flag.NewFlagSet("config context", flag.ContinueOnError)
	proxy := flg.String("proxy", cfg.Proxy, "networks proxy settings")
	repository := flg.String("repo", cfg.Repository, "repo to get access to your models")

	if err := flg.Parse(commandArgs); err == nil {

		pr := *proxy
		r := *repository

		return &ConfigCommand{
			Proxy:      pr,
			Repository: r,
		}, nil
	}

	return nil, errorMessages.ERR_CONFIG
}

func (this *ConfigCommand) Execute() error {

	// create config.yaml if file doesn't exists
	usr, err := user.Current()

	if err != nil {
		println(err)
	}

	filename := ".vortocli.yaml"
	var path string

	if !strings.Contains(runtime.GOOS, "windows") {
		path = usr.HomeDir + "/Documents/" + filename
	} else if strings.Contains(runtime.GOOS, "windows") {
		path = usr.HomeDir + "/" + filename
	}

	errR := os.Remove(path)

	if errR != nil {
		println(errR)
	}

	file, err := os.Create(path)

	if err != nil {
		println("Message:	Error - ", err.Error())
	}

	if !strings.Contains(runtime.GOOS, "windows") {
		file.Chmod(0755)
	}

	content := "proxy: " + this.Proxy +
		"\nrepository: " + this.Repository + "\n"

	n1, err1 := io.WriteString(file, content)

	contentExample := "#proxy: http://myProxy.com:3128\n# repository: http://localhost:8080/infomodelrepository"
	n2, err2 := io.WriteString(file, contentExample)

	if err1 != nil {
		println(n1, err1)
	}
	if err2 != nil {
		println(n2, err2)
	}

	file.Close()

	println("Message:	->	created ", path)

	return nil
}
