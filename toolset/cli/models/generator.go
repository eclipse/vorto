package vorto

/*
	Generator - represents specific generator information
*/
type Generator struct {
	Key              string `json:"key"`
	Name             string `json:"name"`
	Description      string `json:"description"`
	Creator          string `json:"creator"`
	DocumentationUrl string `json:"documentationUrl"`
	ImageUrl32x32    string `json:"image32x32"`
	ImageUrl144x144  string `json:"image144x144"`
	Rating           string `json:"rating"`
	AmountOfDownloads int `json:"amountOfDownloads"`
}
