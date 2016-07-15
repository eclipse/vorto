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
	urlStr := fmt.Sprintf("%v/rest/model/file/%v/%v/%v?output=%v&includeDependencies=%v", this.Repository, this.Namespace, this.Name, this.Version, this.Output, this.IncludeDependencies);
	resp, err := this.Client.MyClient.Get(urlStr)
	if err != nil {
		return errorMessages.ERR_NO_VALID_ID
	} else {
		defer resp.Body.Close()

		contentDisposition := resp.Header.Get("content-disposition")
		var filename string
		fmt.Sscanf(contentDisposition, "attachment; filename = %s", &filename)
		
		// check directory spelling
		if !strings.HasSuffix(this.OutputPath, "\\") {
			this.OutputPath = this.OutputPath + "\\"
		}

		exists(this.OutputPath)

		file, err := os.Create(this.OutputPath + filename)
		if err != nil {
			return err
		}

		defer file.Close()

		size, err := io.Copy(file, resp.Body)
		if err != nil {
			return err
		}

		fmt.Printf("\nMessage: Download successful, %s with %v bytes\n", this.OutputPath + filename, size)
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
