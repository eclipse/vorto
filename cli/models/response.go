package vorto

/*
	Response - represents information after an POST /rest/model/ request, to validate and upload an model
 */
type Response struct {
	ErrorMessage	string 			      `json:"message"`
	Result          []UploadMessageResult `json:"obj"`
}

type UploadMessageResult struct {
	HandleId        string          `json:"handleId"`
	Valid           bool            `json:"valid"`
	ErrorMessage    string          `json:"errorMessage"`
}