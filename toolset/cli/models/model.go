package vorto

/*
	Model - represents general information of an entity, functionblock or information model
 */
type Model struct {
	Id   			ModelItem    	`json:"id"`
	DisplayName		string 			`json:"displayName"`
	Description		string			`json:"description"`
	CreatedBy		string			`json:"author"`
	CreatedOn 		Time			`json:"creationDate"`
	References		[]ModelItem		`json:"references"`
	ReferencedBy	[]ModelItem		`json:"referencedBy"`
}

/*
	ModelItem - represents specific information of an entity, functionblock or information model
 */
type ModelItem struct {
	Name  		 	string 			`json:"name"`
	Namespace 		string 			`json:"namespace"`
	Version 		string 			`json:"version"`
	PrettyFormat 	string 			`json:"prettyFormat"`
	FullPath 		string 			`json:"fullPath"`
}