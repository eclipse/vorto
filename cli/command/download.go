package command

/*
	TODO: - if no path is specified do not add "\"
*/

import (
	"flag"
	"fmt"
	"io"
	"os"
	"strings"

	"github.com/eclipse/vorto/client/cli/config"
	"github.com/eclipse/vorto/client/cli/errorMessages"
)

type DownloadCommand struct {
	Namespace string
	Name      string
	Version   string

	QueryId             string
	Output              string
	OutputPath          string
	IncludeDependencies bool

	Client     *config.Client
	Repository string
}

func (c *DownloadCommand) GetCommandName() string {
	return "download"
}

func NewDownloadCommand(commandValue string, commandArgs []string, cfg *config.Configuration, client *config.Client) (*DownloadCommand, error) {

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

	flg := flag.NewFlagSet("download context", flag.ExitOnError)
	output := flg.String("output", "DSL", "output specifies target platform")
	outputPath := flg.String("outputPath", "", "outputPath specifics download directory")
	includeDependencies := flg.Bool("includeDependencies", false, "includeDependencies resolves all related vorto model dependencies")

	if err := flg.Parse(commandArgs); err == nil {
		o := *output
		oP := *outputPath
		iD := *includeDependencies

		return &DownloadCommand{
			Namespace: namespace,
			Name:      name,
			Version:   version,

			QueryId:             commandValue,
			Output:              o,
			OutputPath:          oP,
			IncludeDependencies: iD,

			Client:     client,
			Repository: cfg.Repository,
		}, nil
	}

	return nil, errorMessages.ERR_NO_DEFAULT_DOWNLOAD_COMMAND
}

func (this *DownloadCommand) Execute() error {

	var resolveAs string
	var datatype string

	if this.IncludeDependencies == true {
		// if flag for -includeDependencies=true resolve all dependencies and download them
		resolveAs = "zip"
		datatype = "zip"
	} else {
		// if flag for -includeDependencies=false don't resolve all dependencies and don't download them
		resolveAs = "file"

		switch {
		case strings.Contains(this.Namespace, "functionblockmodels"):
			datatype = "fbmodel"
		case strings.Contains(this.Namespace, "informationmodels"):
			datatype = "infomodel"
		case strings.Contains(this.Namespace, "type"):
			datatype = "type"
		default:
			return errorMessages.ERR_NO_DATATYPE
		}
	}

	resp, err := this.Client.MyClient.Get(this.Repository + "/rest/model/" + resolveAs + "/" + this.Namespace + "/" + this.Name + "/" + this.Version + "?output=" + this.Output)
	if err != nil {
		return errorMessages.ERR_NO_VALID_ID
	} else {
		defer resp.Body.Close()

		// check directory spelling
		if !strings.HasSuffix(this.OutputPath, "\\") {
			this.OutputPath = this.OutputPath + "\\"
		}

		exists(this.OutputPath)

		file, err := os.Create(this.OutputPath + this.Name + "." + datatype)
		if err != nil {
			return err
		}

		defer file.Close()

		size, err := io.Copy(file, resp.Body)
		if err != nil {
			return err
		}

		println()
		fmt.Printf("Message: Download successful, %swith %v bytes", this.OutputPath+this.Name+"."+datatype+" ", size)
		println()
	}

	return nil
}

/*
	func exists - returns whether the given file or directory exists or not
*/
func exists(path string) bool {
	if _, err := os.Stat(path); os.IsNotExist(err) {
		// path does not exist -> create path
		os.MkdirAll(path, 777)
		println("Created Directory: " + path)
		return true
	}

	if _, err := os.Stat(path); err == nil {
		// path exists
		return true
	}
	return true
}
