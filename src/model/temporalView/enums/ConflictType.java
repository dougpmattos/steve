package model.temporalView.enums;

public enum ConflictType {

	BEGIN_END_DEFINED,
	BEGIN_DEFINED,
	END_DEFINED,
	NEW_BEGIN_GREATER_THAN_EXISTING_END,
	NEW_END_LESS_THAN_EXISTING_BEGIN;
	
}