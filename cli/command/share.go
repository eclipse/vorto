package command

/*
TODO - enable .zip upload
*/

/*
	Upload model - Model uploading is divided in two steps:
	1). POST Request to http://localhost:8080/infomodelrepository/rest/model/
		Request Headers:
			Authorization:Basic base64 'username:password'
			Content-Type:multipart/form-data;
	2). PUT Request to http://localhost:8080/infomodelrepository/rest/model/vorto2793088504099676665Water.type
*/

import (
	"bytes"
	b64 "encoding/base64"
	"encoding/json"
	"errors"
	"flag"
	"io"
	"io/ioutil"
	"mime/multipart"
	"net/http"
	"os"
	"fmt"

	"github.com/eclipse/vorto/client/cli/config"
	"github.com/eclipse/vorto/client/cli/errorMessages"
	"github.com/eclipse/vorto/client/cli/models"
)

type ShareCommand struct {
	Username   string
	Password   string
	File       string
	Repository string
	Client     *config.Client
}

func (c *ShareCommand) GetCommandName() string {
	return "share"
}

func NewShareCommand(commandValue string, commandArgs []string, cfg *config.Configuration, client *config.Client) (*ShareCommand, error) {
	f := commandValue

	flg := flag.NewFlagSet("share context", flag.ContinueOnError)
	username := flg.String("username", cfg.Username, "users username for login")
	password := flg.String("password", cfg.Password, "users password for login")

	if err := flg.Parse(commandArgs); err == nil {

		u := *username
		p := *password

		switch {
		case u == "" || p == "":
			return nil, errors.New("either specify user data in {$Home_Dir/vortocli/config.yaml} or via flags -username user -password xxxx")
		case u == "":
			return nil, errors.New("either specify username in {$Home_Dir/vortocli/config.yaml} or via flag -username user")
		case p == "":
			return nil, errors.New("either specify password in {$Home_Dir/vortocli/config.yaml} or via flag -password xxxx")
		}

		return &ShareCommand{
			Username:   u,
			Password:   p,
			File:       f,
			Repository: cfg.Repository,
			Client:     client,
		}, nil
	}

	return nil, errorMessages.ERR_NO_DEFAULT_SHARE_COMMAND
}

func (this *ShareCommand) Execute() error {

	// if user sets username and password flag, the programme ignores the config settings
	// username = strings.Replace(username, " ", "", -1)

	// 	Authentication requires base64 encoding 'username:password'
	sEncCredentials := b64.StdEncoding.EncodeToString([]byte(this.Username + ":" + this.Password))

	// 	pack file and post it to /rest/model
	var b bytes.Buffer
	w := multipart.NewWriter(&b)
	f, err := os.Open(this.File)
	if err != nil {
		return err
	}

	fw, err := w.CreateFormFile("file", this.File)
	if err != nil {
		return err
	}

	if _, err = io.Copy(fw, f); err != nil {
		return err
	}

	if fw, err = w.CreateFormField("key"); err != nil {
		return err
	}

	if _, err = fw.Write([]byte("KEY")); err != nil {
		return err
	}

	w.Close()

	req, e := http.NewRequest("POST", this.Repository+"/rest/secure/", &b)
	req.Header.Set("Content-Type", w.FormDataContentType())
	req.Header.Set("Authorization", "Basic "+sEncCredentials)

	if e != nil {
		return err
	}

	resp, err := this.Client.MyClient.Do(req)

	var errorMessage string
	var handleId string
	var valid bool

	if resp.StatusCode == 200 {
		if err != nil {
			return err
		} else {
			defer resp.Body.Close()
			contents, err := ioutil.ReadAll(resp.Body)
			if err != nil {
				return err
			} else {
				body := &bytes.Buffer{}
				_, err := body.ReadFrom(resp.Body)
				if err != nil {
					return err
				}
				resp.Body.Close()
			}
			
			var response vorto.Response
			if err := json.Unmarshal(contents, &response); err != nil {
				return errorMessages.ERR_AUTH_FAILED
			}

			errorMessage = response.Result[0].ErrorMessage
			handleId = response.Result[0].HandleId
			valid = response.Result[0].Valid
		}
		
		// only if model is valid 'valid=true', user should be able to upload his model
		if valid {
			request, err := http.NewRequest("PUT", this.Repository+"/rest/secure/"+handleId, &b)
			request.Header.Set("Authorization", "Basic "+sEncCredentials)

			if err != nil {
				return err
			}

			response, err := this.Client.MyClient.Do(request)
			if err != nil {
				return err
			} else {

				body := &bytes.Buffer{}
				_, err := body.ReadFrom(response.Body)
				if err != nil {
					return err
				}
				response.Body.Close()

				if response.StatusCode == 200 {
					println("Message:	Checkin of " + this.File + " successful")
					return nil
				} else {
					return errorMessages.ERR_UNKNOWN
				}
			}
		} else {
			return errors.New(errorMessage)
		}
	} else if resp.StatusCode == 401 {
		return errorMessages.ERR_HTTP_401
	} else {
		return errors.New(fmt.Sprintln("Error Status Code: ", resp.StatusCode))
	}
}
