package config

import (
	"io"
	"os"
	"os/user"
	"runtime"
	"strings"

	"github.com/kylelemons/go-gypsy/yaml"
)

type Configuration struct {
	Username   string `yaml:"username"`
	Password   string `yaml:"password"`
	Proxy      string `yaml:"proxy"`
	Repository string `yaml:"repository"`
}

func NewConfiguration() *Configuration {

	data := initializeConfigYaml()

	var config Configuration

	username, errUsername := data.Get("username")
	password, errPassword := data.Get("password")
	proxy, errProxy := data.Get("proxy")
	repository, errRepository := data.Get("repository")

	if errUsername != nil {
		username = ""
	}

	if errPassword != nil {
		password = ""
	}

	if errProxy != nil {
		proxy = ""
	}

	if errRepository != nil {
		repository = ""
	}

	config.Username = username
	config.Password = password
	config.Proxy = proxy
	config.Repository = repository

	// configure default repository
	if config.Repository != "" {
		if strings.HasSuffix(config.Repository, "/") {
			config.Repository = config.Repository[:len(config.Repository)-1]
		}
	} else {
		//default repo
		config.Repository = "http://vorto.eclipse.org/repo/"
	}

	return &config
}

func (this *Configuration) GetConfigurationUsername() string {
	return this.Username
}
func (this *Configuration) GetConfigurationPassword() string {
	return this.Password
}
func (this *Configuration) GetConfigurationProxy() string {
	return this.Proxy
}
func (this *Configuration) GetConfigurationRepository() string {
	return this.Repository
}

/*
	TODO - trim whitespaces if they exists as prefix or suffix
*/
func initializeConfigYaml() *yaml.File {

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

	if _, err := os.Stat(path); os.IsNotExist(err) {

		file, err := os.Create(path)

		if err != nil {
			println("Message:	Error - ", err.Error())
		}

		if !strings.Contains(runtime.GOOS, "windows") {
			file.Chmod(0755)
		}

		content := "username: \npassword: \nproxy: \nrepository: \n"
		n1, err1 := io.WriteString(file, content)

		contentExample := "# username: models\n# password: models\n# proxy: http://myProxy.com:3128\n# repository: http://localhost:8080/infomodelrepository"
		n2, err2 := io.WriteString(file, contentExample)

		if err1 != nil {
			println(n1, err1)
		}
		if err2 != nil {
			println(n2, err2)
		}

		file.Close()
	}

	// read config.yaml
	data, err := yaml.ReadFile(path)
	if err != nil {
		println("Message: ", err)
	}

	return data
}
