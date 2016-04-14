package vorto

/*
	Response - represents information after an POST /rest/model/ request, to validate and upload an model
 */
type Response struct {
	ErrorMessage	string 			`json:"errorMessage"`
	HandleId  		string 			`json:"handleId"`
	Valid			bool			`json:"valid"`
}