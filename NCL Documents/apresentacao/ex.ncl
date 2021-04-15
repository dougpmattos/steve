<?xml version="1.0" encoding="ISO-8859-1"?>
<ncl id="ex" xmlns="http://www.ncl.org.br/NCL3.0/EDTVProfile">

<head>
	
	<regionBase>	
		<region id="tvReg">
			<region id="screenReg" zIndex="1"/>
			<region id="botaoReg" left="5%" bottom="5%" width="200" height="55" zIndex="1"/>
		</region>
	</regionBase>

	<descriptorBase>
		<descriptor id="screenDesc" region="screenReg"/>
		<descriptor id="botaoDesc" region="botaoReg" explicitDur="5s"/>
		<descriptor id="infosDesc" region="screenReg" explicitDur="3s"/>
	</descriptorBase>
	
	<connectorBase>
		<causalConnector id="onBeginStartDelay">
			<connectorParam name="delay"/>				
			<simpleCondition role="onBegin"/>
			<simpleAction role="start" delay="$delay"/>
		</causalConnector>
		<causalConnector id="onKeySelectionStopStart">
			<connectorParam name="key"/>
			<simpleCondition role="onSelection" key="$key"/>
			<compoundAction operator="seq">
				<simpleAction role="stop"/>
				<simpleAction role="start"/>					
			</compoundAction>				
		</causalConnector>
	</connectorBase>

</head>

<body>	
	
	<port id="entry" component="video"/>
	
	<mediaNode id="video" src="video.mpg" descriptor="screenDesc"/>
	<mediaNode id="botao" src="botao.png" descriptor="botaoDesc"/>
	<mediaNode id="infos" src="infos.png" descriptor="infosDesc"/>
	

	<link id="lVideo" xconnector="onBeginStartDelay">
		<bind role="onBegin" component="video"/>
		<bind role="start" component="botao">
			<bindParam name="delay" value="2s"/>
		</bind>
	</link>

	<link id="lInfos" xconnector="onKeySelectionStopStart">
		<bind role="onSelection" component="botao">
			<bindParam name="key" value="RED"/>
		</bind>
		<bind role="stop" component="botao"/>
		<bind role="start" component="infos"/>
	</link>


</body>

</ncl>
