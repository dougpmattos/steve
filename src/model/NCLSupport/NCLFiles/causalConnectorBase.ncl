<?xml version="1.0" encoding="ISO-8859-1"?>

<ncl id="causalConnBase" xmlns="http://www.ncl.org.br/NCL3.0/EDTVProfile">

	<head>

		<connectorBase id="causalConnectorBase">

			<causalConnector id="onBeginStart">
				<simpleCondition role="onBegin"/>
				<simpleAction role="start" max="unbounded"/>
			</causalConnector>

			<causalConnector id="onBeginStartDelay">
				<connectorParam name="delay"/>
				<simpleCondition role="onBegin"/>
				<simpleAction role="start" delay="$delay" max="unbounded"/>
			</causalConnector>

			<causalConnector id="onEndStop">
				<simpleCondition role="onEnd"/>
				<simpleAction role="stop" max="unbounded"/>
			</causalConnector>

			<causalConnector id="onEndStopDelay">
				<connectorParam name="delay"/>
				<simpleCondition role="onEnd"/>
				<simpleAction role="stop" delay="$delay" max="unbounded"/>
			</causalConnector>

			<causalConnector id="onEndStart">
				<simpleCondition role="onEnd"/>
				<simpleAction role="start" max="unbounded"/>
			</causalConnector>

			<causalConnector id="onEndStartDelay">
				<connectorParam name="delay"/>
				<simpleCondition role="onEnd"/>
				<simpleAction role="start" delay="$delay" max="unbounded"/>
			</causalConnector>

			<causalConnector id="onBeginStop">
				<simpleCondition role="onBegin"/>
				<simpleAction role="stop" max="unbounded"/>
			</causalConnector>

			<causalConnector id="onBeginStopDelay">
				<connectorParam name="delay"/>
				<simpleCondition role="onBegin"/>
				<simpleAction role="stop" delay="$delay" max="unbounded"/>
			</causalConnector>
			
			<causalConnector id="onSelectionStartStop">
				<simpleCondition role="onSelection"/>
				<simpleAction role="start" max="unbounded"/>
				<simpleAction role="stop" max="unbounded"/>
			</causalConnector>

		</connectorBase>

	</head>

</ncl>