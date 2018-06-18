package command

import (
	"fmt"
)

type HelpCommand struct{}

func (c *HelpCommand) GetCommandName() string {
	return "help"
}

func NewHelpCommand() (*HelpCommand, error) {

	return &HelpCommand{}, nil
}

func (this *HelpCommand) Execute() error {

	fmt.Println()
	fmt.Println("Commands -------------------------------------------------------------------------------------------------------------------------")
	fmt.Println("vorto [info|query|download|generate|generators] [default value] -optional flag parameters")

	fmt.Println()

	fmt.Println("vorto [info] [value] -------------------------------------------------------------------------------------------------------------")
	fmt.Println()
	fmt.Println("  -Example: vorto info examples.datatypes.event.EventType:1.0.0")

	fmt.Println()

	fmt.Println("vorto [query] [value] ------------------------------------------------------------------------------------------------------------")
	fmt.Println()
	fmt.Println("  -Example: vorto query \"*\"")

	fmt.Println()

	fmt.Println("vorto [download] [value] -optional flag parameters -------------------------------------------------------------------------------")
	fmt.Println()
	fmt.Println("  -output		(Default: 'DSL'		| output specifies target platform)")
	fmt.Println("  -outputPath		(Default: -		| outputPath specifics download directory)")
	fmt.Println("  -includeDependencies	(Default: 'false'	| includeDependencies resolves all related vorto model dependencies)")
	fmt.Println()
	fmt.Println("  -Example: vorto download examples.datatypes.event.EventType:1.0.0 -output DSL -outputPath mydir -includeDependencies")

	fmt.Println()

	fmt.Println("vorto [generate] [value] -optional flag parameters -------------------------------------------------------------------------------")
	fmt.Println()
	fmt.Println("  -generatorKey		(Default: -		| generatorKey specifies the platform generator)")
	fmt.Println("  -outputPath		(Default: -		| outputPath specifics the download directory)")
	fmt.Println()
	fmt.Println("  -Example: vorto generate examples.informationmodels.sensors.TI_SensorTag_CC2650:1.0.0 -generatorKey mqtt -outputPath D:\\models")

	fmt.Println()

	fmt.Println("vorto [generators] ---------------------------------------------------------------------------------------------------------------")
	fmt.Println()
	fmt.Println("  -Example: vorto generators")

	fmt.Println()
	fmt.Println()

	return nil
}
