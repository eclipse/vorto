// UnitEntity

#include "UnitEntity.h"

using namespace org_eclipse_vorto;

UnitEntity::UnitEntity(){}

void UnitEntity::setvalue(float _value) {
    value = _value;
}

float UnitEntity::getvalue() {
    return value;
}
void UnitEntity::setunitEnum(org_eclipse_vorto::UnitEnum _unitEnum) {
    unitEnum = _unitEnum;
}

org_eclipse_vorto::UnitEnum UnitEntity::getunitEnum() {
    return unitEnum;
}

String UnitEntity::serialize() {
    String result = "\"UnitEntity\": {";
        result += "\"value\": " + String(value) + ",";
        result += "\"unitEnum\": " + String(unitEnum);
        result += "}";

    return result;
}
