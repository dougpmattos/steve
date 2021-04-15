<?xml version="1.0" encoding="ISO-8859-1"?>
<ncl id="musicAd" xmlns="http://www.ncl.org.br/NCL3.0/EDTVProfile">

<head>
	
	<regionBase>	
		<region id="tvReg">
			<region id="screenReg" zIndex="1"/>
			<region id="endReg" zIndex="2"/>
			<region id="logoReg" left="5%" bottom="5%" width="200" height="55" zIndex="3"/>	
			<region id="iconReg" left="5%" bottom="5%" width="200" height="55" zIndex="3"/>	
			<region id="productReg" left="25%" top="20%" width="250" height="250" zIndex="3"/>
			<region id="priceReg" left="60%" top="40%" width="150" height="150" zIndex="3"/>
		</region>
	</regionBase>

	<descriptorBase>
		<descriptor id="screenDesc" region="screenReg"/>
		<descriptor id="audioDesc"/>
		<descriptor id="logoDesc" region="logoReg"/>
		<descriptor id="iconDesc" region="iconReg" explicitDur="3s"/>
		<descriptor id="endDesc" region="endReg" explicitDur="5s"/>
		<descriptor id="productDesc" region="productReg"/>
		<descriptor id="priceDesc" region="priceReg"/>
		<descriptor id="productInfosDesc" region="screenReg" explicitDur="10s"/>
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
		<causalConnector id="onBeginStartDelay">
			<connectorParam name="delay"/>				
			<simpleCondition role="onBegin"/>
			<simpleAction role="start" delay="$delay"/>
		</causalConnector>
		<causalConnector id="onEndStop">
			<simpleCondition role="onEnd"/>
			<simpleAction role="stop"/>
		</causalConnector>
		<causalConnector id="onEndStartN">
			<simpleCondition role="onEnd"/>
			<simpleAction role="start" max="unbounded" qualifier="par"/>
		</causalConnector>
		<causalConnector id="onEndStartStop">
			<simpleCondition role="onEnd"/>
			<compoundAction operator="seq">
				<simpleAction role="start"/>
				<simpleAction role="stop"/>
			</compoundAction>	
		</causalConnector>
		<causalConnector id="onBeginTestStart">
			<connectorParam name="var"/>			
			<compoundCondition operator="and">
				<simpleCondition role="onBegin"/>
			    <assessmentStatement comparator="eq">
					<attributeAssessment role="testVar" attributeType="nodeProperty" eventType="attribution"/>
					<valueAssessment value="$var"/>
				</assessmentStatement>
			</compoundCondition>	
			<simpleAction role="start"/>
		</causalConnector>
		<causalConnector id="onKeySelectionStopSetStart">
			<connectorParam name="key"/>
			<simpleCondition role="onSelection" key="$key"/>
			<compoundAction operator="seq">
				<simpleAction role="stop"/>
				<simpleAction role="set" value="5%,6.67%,45%,45%"/>
				<simpleAction role="start"/>					
			</compoundAction>	
		</causalConnector>
	</connectorBase>

</head>

<body>	
	
	<port id="entry" component="introduction"/>
	
	<mediaNode id="introduction" src="introduction.mpg" descriptor="screenDesc">
	</mediaNode>
	<mediaNode id="audio" src="audio.mp3" descriptor="audioDesc">
			<area id="anchorProduct1" begin="5s" end="9s"/>
			<area id="anchorProduct2" begin="10s" end="14s"/>
	</mediaNode>
	
	<mediaNode id="audio2" src="audio.mp3" descriptor="audioDesc">
	</mediaNode>
	
	<mediaNode id="background" src="background.png" descriptor="screenDesc"/>
	<mediaNode id="icon" src="icon.png" descriptor="iconDesc"/>
	<mediaNode id="logo" src="logo.png" descriptor="logoDesc"/>
	<mediaNode id="end" src="end.png" descriptor="endDesc">
		<property name="bounds"/>
	</mediaNode>
	<mediaNode id="productInfos" src="productInfos.png" descriptor="productInfosDesc"/>
	<mediaNode id="globalVar" type="application/x-ginga-settings">
		<property name="channel.interactivity" value="true"/>
	</mediaNode>
	
	<context id="contextProduct">
		<mediaNode id="product1" src="product1.png" descriptor="productDesc"/>
		<mediaNode id="price1" src="price1.png" descriptor="priceDesc"/>
		<mediaNode id="reusedAudio" refer="audio" instance="instSame"/>
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
		
		
	
		<mediaNode id="product2" src="product2.png" descriptor="productDesc"/>
		<mediaNode id="price2" src="price2.png" descriptor="priceDesc"/>
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

	</context>
	
	<link id="lAudio" xconnector="onBeginStartN">
		<bind role="onBegin" component="introduction"/>
		<bind role="start" component="audio2"/>
	</link>
	
	<link id="lLogo" xconnector="onBeginStartDelay">
		<bind role="onBegin" component="introduction"/>
		<bind role="start" component="logo">
			<bindParam name="delay" value="1s"/>
		</bind>
	</link>
	
	<link id="lEnd" xconnector="onEndStop">
		<bind role="onEnd" component="introduction"/>
		<bind role="stop" component="logo"/>
	</link>

	<link id="lAudio" xconnector="onEndStartN">
		<bind role="onEnd" component="introduction"/>
		<bind role="start" component="audio"/>
		<bind role="start" component="background"/>
	</link>
	
	<link id="lEndBackground" xconnector="onEndStartStop">
		<bind role="onEnd" component="audio"/>
		<bind role="start" component="end"/>
		<bind role="stop" component="background"/>
	</link>
	
	<link id="lIcon" xconnector="onBeginTestStart">
		<bind role="onBegin" component="end"/>
		<bind role="testVar" component="globalVar" interface="channel.interactivity">
			<bindParam name="var" value="true"/>
		</bind>
		<bind role="start" component="icon"/>
	</link>

	<link id="lProductInfos" xconnector="onKeySelectionStopSetStart">
		<bind role="onSelection" component="icon">
			<bindParam name="key" value="RED"/>
		</bind>
		<bind role="stop" component="icon"/>
		<bind role="set" component="end" interface="bounds"/>
		<bind role="start" component="productInfos"/>
	</link>


</body>

</ncl>
