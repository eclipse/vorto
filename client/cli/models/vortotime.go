package vorto
import (
	"time"
)

type Time struct {
    time.Time
}

// returns time.Now() no matter what!
func (t *Time) UnmarshalJSON(b []byte) error {
	t1, _ := time.Parse("\"2006-01-02T15:04:05.999-0700\"", string(b[:]))
    
	*t = Time{t1}

    return nil
}