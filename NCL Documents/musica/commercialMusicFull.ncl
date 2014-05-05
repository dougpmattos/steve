<?xml version="1.0" encoding="ISO-8859-1"?>
<ncl id="musicCommercial" xmlns="http://www.ncl.org.br/NCL3.0/EDTVProfile">

<head>
	
	<regionBase>	
		<region id="tvReg">
			<region id="screenReg" zIndex="1"/>
			<region id="logoReg" left="5%" bottom="5%" width="200" height="55" zIndex="2"/>	
			<region id="iconReg" left="5%" bottom="5%" width="200" height="55" zIndex="2"/>	
			<region id="productReg" left="25%" top="20%" width="250" height="250" zIndex="2"/>
			<region id="priceReg" left="60%" top="40%" width="150" height="150" zIndex="2"/>
			<region id="description1Reg" left="60%" top="40%" width="100" height="100" zIndex="2"/>
			<region id="description2Reg" left="60%" top="70%" width="100" height="100" zIndex="2"/>
			<region id="description3Reg" left="60%" top="100%" width="100" height="100" zIndex="2"/>
		</region>
	</regionBase>

	<descriptorBase>
		<descriptor id="screenDesc" region="screenReg"/>
		<descriptor id="openingDesc" region="screenReg"/>
		<descriptor id="audioDesc"/>
		<descriptor id="logoDesc" region="logoReg"/>
		<descriptor id="iconDesc" region="iconReg"/>
		<descriptor id="endDesc" region="screenReg" explicitDur="5s"/>
		<descriptor id="productDesc" region="productReg"/>
		<descriptor id="priceDesc" region="priceReg"/>
		<descriptor id="description1Desc" region="description1Reg"/>
		<descriptor id="description2Desc" region="description2Reg"/>
		<descriptor id="description3Desc" region="description3Reg"/>
		<descriptor id="secondBackgroundDesc" region="screenReg" explicitDur="10s"/>
	</descriptorBase>
	
	<connectorBase>
		<causalConnector id="onBeginStartN">
			<simpleCondition role="onBegin"/>
			<simpleAction role="start" max="unbounded" qualifier="par"/>
		</causalConnector>
		<causalConnector id="onEndStopN">
			<simpleCondition role="onEnd"/>
			<simpleAction role="stop" max="unbounded" qualifier="par"/>
		</causalConnector>
		<causalConnector id="onBeginStartNSet">
			<connectorParam name="var"/>
			<simpleCondition role="onBegin"/>
			<compoundAction operator="seq">
				<simpleAction role="start" max="unbounded" qualifier="par"/>
				<simpleAction role="set" value="$var"/>
			</compoundAction>	
		</causalConnector>
		<causalConnector id="onEndStart">
			<simpleCondition role="onEnd"/>
			<simpleAction role="start"/>
		</causalConnector>
		<causalConnector id="onBeginStart">
			<simpleCondition role="onBegin"/>
			<simpleAction role="start"/>
		</causalConnector>
		<causalConnector id="onEndStop">
			<simpleCondition role="onEnd"/>
			<simpleAction role="stop"/>
		</causalConnector>
		<causalConnector id="onEndStartN">
			<simpleCondition role="onEnd"/>
			<simpleAction role="start" max="unbounded" qualifier="par"/>
		</causalConnector>
		<causalConnector id="onBeginTestStartDelay">
			<connectorParam name="var"/>
			<connectorParam name="delay"/>				
			<compoundCondition operator="and">
				<simpleCondition role="onBegin"/>
			    <assessmentStatement comparator="eq">
					<attributeAssessment role="testVar" attributeType="nodeProperty" eventType="attribution"/>
					<valueAssessment value="$var"/>
				</assessmentStatement>
			</compoundCondition>	
			<simpleAction role="start" delay="$delay"/>
		</causalConnector>
		<causalConnector id="onKeySelectionStopAbortStart">
			<connectorParam name="key"/>
			<simpleCondition role="onSelection" key="$key"/>
			<compoundAction operator="seq">
				<simpleAction role="stop"/>
				<simpleAction role="abort"/>
				<simpleAction role="start"/>					
			</compoundAction>	
		</causalConnector>
		<causalConnector id="onEndStartStop">
			<simpleCondition role="onEnd"/>
			<compoundAction operator="seq">
				<simpleAction role="start"/>
				<simpleAction role="stop"/>
			</compoundAction>	
		</causalConnector>
	</connectorBase>

</head>

<body>	
	
	<port id="entry" component="introduction"/>
	
	<media id="introduction" src="introduction.mpg" descriptor="screenDesc"/>
	<media id="opening" src="opening.png" descriptor="openingDesc"/>
	<media id="audio" src="audio.mp3" descriptor="audioDesc">
			<area id="anchorOpening" begin="0s" end="4s"/>
			<area id="anchorProduct1" begin="5s" end="9s"/>
			<area id="anchorProduct2" begin="10s" end="14s"/>
			<area id="anchorProduct3" begin="15s" end="19s"/>
	</media>
	<media id="firstBackground" src="firstBackground.png" descriptor="screenDesc"/>
	<media id="logo" src="logo.png" descriptor="logoDesc"/>
	<media id="final" src="final.mpg" descriptor="screenDesc"/>
	<media id="icon" src="icon.png" descriptor="iconDesc"/>
	<media id="end" src="end.png" descriptor="endDesc"/>
	<media id="secondBackground" src="secondBackground.png" descriptor="secondBackgroundDesc"/>
	<media id="globalVar" type="application/x-ginga-settings">
		<property name="channel.interactivity" value="true"/>
	</media>
	
	<context id="contextProduct">
		<media id="product1" src="product1.png" descriptor="productDesc"/>
		<media id="price1" src="price1.png" descriptor="priceDesc"/>
		<media id="reusedAudio" refer="audio" instance="instSame"/>
		<link id="lProduct1" xconnector="onBeginStartN">
			<bind role="onBegin" component="reusedAudio" interface="anchorProduct1"/>
			<bind role="start" component="product1"/>
			<bind role="start" component="price1"/>
		</link>
		<link id="lEndProduct1" xconnector="onEndStopN">
			<bind role="onEnd" component="reusedAudio" interface="anchorProduct1"/>
			<bind role="stop" component="product1"/>
			<bind role="stop" component="price1"/>
		</link>
		
		
	
		<media id="product2" src="product2.png" descriptor="productDesc"/>
		<media id="price2" src="price2.png" descriptor="priceDesc"/>
		<link id="lProduct2" xconnector="onBeginStartN">
			<bind role="onBegin" component="reusedAudio" interface="anchorProduct2"/>
			<bind role="start" component="product2"/>
			<bind role="start" component="price2"/>
		</link>
		<link id="lEndProduct2" xconnector="onEndStopN">
			<bind role="onEnd" component="reusedAudio" interface="anchorProduct2"/>
			<bind role="stop" component="product2"/>
			<bind role="stop" component="price2"/>
		</link>
		
	
	
		<media id="product3" src="product3.png" descriptor="productDesc"/>
		<media id="price3" src="price3.png" descriptor="priceDesc"/>
		<link id="lProduct3" xconnector="onBeginStartN">
			<bind role="onBegin" component="reusedAudio" interface="anchorProduct3"/>
			<bind role="start" component="product3"/>
			<bind role="start" component="price3"/>
		</link>
		<link id="lEndProduct3" xconnector="onEndStopN">
			<bind role="onEnd" component="reusedAudio" interface="anchorProduct3"/>
			<bind role="stop" component="product3"/>
			<bind role="stop" component="price3"/>
		</link>
	</context>

	<link id="lAudio" xconnector="onEndStart">
		<bind role="onEnd" component="introduction"/>
		<bind role="start" component="audio"/>
	</link>
	
	<link id="lOpening" xconnector="onBeginStart">
		<bind role="onBegin" component="audio" interface="anchorOpening"/>
		<bind role="start" component="opening"/>
	</link>
	
	<link id="lEndOpening" xconnector="onEndStop">
		<bind role="onEnd" component="audio" interface="anchorOpening" />
		<bind role="stop" component="opening" />
	</link>
	
	<link id="lFirstBackgroundLogo" xconnector="onEndStartN">
		<bind role="onEnd" component="opening"/>
		<bind role="start" component="firstBackground"/>
		<bind role="start" component="logo"/>
	</link>
	
	<link id="lEndFirstBackgroundLogo" xconnector="onEndStopN">
		<bind role="onEnd" component="audio"/>
		<bind role="stop" component="firstBackground"/>
		<bind role="stop" component="logo"/>
	</link>
	
	<link id="lFinal" xconnector="onEndStartN">
		<bind role="onEnd" component="audio"/>
		<bind role="start" component="final"/>
	</link>
	
	<link id="lIcon" xconnector="onBeginTestStartDelay">
		<bind role="onBegin" component="final"/>
		<bind role="testVar" component="globalVar" interface="channel.interactivity">
			<bindParam name="var" value="true"/>
		</bind>
		<bind role="start" component="icon">
			<bindParam name="delay" value="1s"/>
		</bind>
	</link>
	
	<link id="lSecondBackground" xconnector="onKeySelectionStopAbortStart">
		<bind role="onSelection" component="icon">
			<bindParam name="key" value="RED"/>
		</bind>
		<bind role="stop" component="icon"/>
		<bind role="abort" component="final"/>
		<bind role="start" component="secondBackground"/>
	</link>
	
	<link id="lBeginEnd" xconnector="onEndStartStop">
		<bind role="onEnd" component="final"/>
		<bind role="start" component="end"/>
		<bind role="stop" component="icon"/>
	</link>
	
	<link id="lEndSecondBackground" xconnector="onEndStart">
		<bind role="onEnd" component="secondBackground"/>
		<bind role="start" component="end"/>
	</link>

</body>

</ncl>
