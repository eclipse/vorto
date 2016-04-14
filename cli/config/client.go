package config

/*
	Client-Configuration:
	depending on your config.yaml it creates a proxy or not
*/

import (
	"net/http"
	"net/url"
)

type Client struct {
	MyClient http.Client
}

func (this *Client) SetMyClient(myClient http.Client) {
	this.MyClient = myClient
}
func (this *Client) GetMyClient() http.Client {
	return this.MyClient
}

func NewClient(conf *Configuration) *Client {

	myClient := http.Client{}

	// proxyUrl http://cache.innovations.de:3128
	if conf.Proxy != "" {
		proxyUrl, err := url.Parse(conf.Proxy)
		if err != nil {
			println(err.Error())
		}
		myClient = http.Client{Transport: &http.Transport{Proxy: http.ProxyURL(proxyUrl)}}
	}

	return &Client{myClient}
}
