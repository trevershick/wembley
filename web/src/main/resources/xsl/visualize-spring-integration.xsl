<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:beans="http://www.springframework.org/schema/beans"
	xmlns:batch="http://www.springframework.org/schema/batch"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:p="http://www.springframework.org/schema/p"
	xmlns:tx="http://www.springframework.org/schema/tx"
	xmlns:int="http://www.springframework.org/schema/integration"
	xmlns:int-stream="http://www.springframework.org/schema/integration/stream"
	xmlns:si-xml="http://www.springframework.org/schema/integration/xml"
	xmlns:jms="http://www.springframework.org/schema/jms"
	xmlns:v="http://com.github.trevershick/schemas/visualize">

	<xsl:output encoding="UTF-8" indent="no" method="text" />

	<xsl:template match="/beans:beans">
digraph {
	rankdir=LR;
	node[shape=box,style=filled];
	label="<xsl:value-of select="@id"/>";
	<xsl:apply-templates select="//int:channel"/>
	<xsl:apply-templates select="//int:publish-subscribe-channel"/>
	
	<xsl:apply-templates select="int:gateway"/>
	<xsl:apply-templates select="int:service-activator"/>
	<xsl:apply-templates select="int:router"/>
	<xsl:apply-templates select="int:payload-type-router"/>
	<xsl:apply-templates select="int:bridge"/>
	<xsl:apply-templates select="int:transformer"/>
	<xsl:apply-templates select="si-xml:unmarshalling-transformer"/>
	<xsl:apply-templates select="si-xml:marshalling-transformer"/>

	<xsl:apply-templates select="int:chain"/>
	

		{rank=same;<xsl:for-each select="int:gateway"><xsl:value-of select="concat('gw_',@id)"/>,</xsl:for-each>}

}
</xsl:template>



<xsl:template match="int:channel">
	<xsl:variable name="id" select="@id"/>
	<xsl:variable name="nodeName" select="concat('channel_',$id)"/>
	<xsl:value-of select="$nodeName"/> [label="&lt;&lt;Channel&gt;&gt;\n<xsl:value-of select="$id"/>"];
	<!--<xsl:value-of select="$nodeName"/> [height=1.05,shape=plaintext,image="/Users/trevershick/Desktop/channel.png",labelloc="b",style=unfilled,fontcolor=black];-->
	
	<xsl:choose>
		<xsl:when test="int:queue">
			<xsl:value-of select="$nodeName"/> [label="&lt;&lt;Queued Channel&gt;&gt;\n<xsl:value-of select="$id"/>",fillcolor=blue];
		</xsl:when>
		<xsl:otherwise>
		</xsl:otherwise>
	</xsl:choose>
	
</xsl:template>





<xsl:template match="int:gateway">
	<xsl:variable name="id" select="@id"/>
	<xsl:variable name="serviceInterface" select="@service-interface"/>
	<xsl:variable name="defaultRequestChannel" select="@default-request-channel"/>

	gw_<xsl:value-of select="$id"/> [label="&lt;&lt;gateway&gt;&gt;\n <xsl:value-of select="$id"/>\n<xsl:value-of select="$serviceInterface"/>",fillcolor=orange];
	
	<xsl:for-each select="int:method">
	<xsl:variable name="rc" select="@request-channel"/>
	gw_<xsl:value-of select="$id"/> -> channel_<xsl:value-of select="$rc"/> [label="<xsl:value-of select="@name"/>"];
	</xsl:for-each>
	
	<xsl:if test="string-length($defaultRequestChannel) > 0">
	gw_<xsl:value-of select="$id"/> -> channel_<xsl:value-of select="$defaultRequestChannel"/>;
	</xsl:if>
</xsl:template>



<xsl:template match="int:service-activator">
	<xsl:variable name="id" select="@id"/>
	<xsl:variable name="inputChannel" select="@input-channel"/>
	<xsl:variable name="expression" select="@expression"/>
	<xsl:variable name="nodeName" select="concat('sa_', $inputChannel)"/>
	
	<xsl:choose>
		<xsl:when test="@ref">
			<xsl:value-of select="$nodeName"/> [fillcolor=green,label="&lt;&lt;Service Activator&gt;&gt;\n<xsl:value-of select="@ref"/>.<xsl:value-of select="@method"/>"];
		</xsl:when>
		<xsl:when test="@expression">
			<xsl:value-of select="$nodeName"/> [fillcolor=green,label="&lt;&lt;Service Activator&gt;&gt;\n<xsl:value-of select="@expression"/>"];
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select="$nodeName"/> [fillcolor=green,label="&lt;&lt;Service Activator&gt;&gt;"];
		</xsl:otherwise>
	</xsl:choose>
	
	channel_<xsl:value-of select="$inputChannel"/> -> <xsl:value-of select="$nodeName"/> [label="input-channel"];

</xsl:template>

<xsl:template match="int:publish-subscribe-channel">
	<xsl:variable name="id" select="@id"/>
	<xsl:variable name="nodeName" select="concat('channel_', $id)"/>
	
	<xsl:value-of select="$nodeName"/> [label="&lt;&lt;Pub Sub Channel&gt;&gt;\n<xsl:value-of select="$id"/>",fillcolor="yellow"];
</xsl:template>

<xsl:template match="int:router">
	<xsl:variable name="id" select="@id"/>
	<xsl:variable name="inputChannel" select="@input-channel"/>
	<xsl:variable name="nodeName" select="concat('router_', $inputChannel)"/>

	<xsl:value-of select="$nodeName"/> [label="&lt;&lt;Router&gt;&gt;",fillcolor="red"];
	<xsl:if test="string-length($inputChannel) > 0">
		channel_<xsl:value-of select="$inputChannel"/> -> <xsl:value-of select="$nodeName"/>;
	</xsl:if>
</xsl:template>

<xsl:template match="int:payload-type-router">
	<xsl:variable name="id" select="@id"/>
	<xsl:variable name="inputChannel" select="@input-channel"/>
	<xsl:variable name="nodeName" select="concat('router_', $inputChannel)"/>

	<xsl:value-of select="$nodeName"/> [label="&lt;&lt;Router&gt;&gt;",fillcolor="red"];
	<xsl:if test="string-length($inputChannel) > 0">
		channel_<xsl:value-of select="$inputChannel"/> -> <xsl:value-of select="$nodeName"/>;
	</xsl:if>
	
	<xsl:for-each select="int:mapping">
		<xsl:value-of select="$nodeName"/> -> channel_<xsl:value-of select="@channel"/> [label="<xsl:value-of select="v:simpleClassName(@type)"/>"];
	</xsl:for-each>
</xsl:template>

<xsl:template match="int:transformer">
	<xsl:variable name="id" select="@id"/>
	<xsl:variable name="inputChannel" select="@input-channel"/>
	<xsl:variable name="outputChannel" select="@output-channel"/>
	<xsl:variable name="nodeName" select="concat('xformer_', $id)"/>
	<xsl:variable name="xformer" select="$nodeName"/>
	
	<xsl:value-of select="$xformer"/> [label="&lt;&lt;Transformer&gt;&gt;\n<xsl:value-of select="$id"/>"];
	<xsl:if test="string-length($inputChannel) > 0">
	channel_<xsl:value-of select="$inputChannel"/> -> <xsl:value-of select="$xformer"/>;
	</xsl:if>
	
	<xsl:if test="string-length($outputChannel) > 0">
	<xsl:value-of select="$xformer"/> -> channel_<xsl:value-of select="$outputChannel"/>;
	</xsl:if>
</xsl:template>


<xsl:template match="si-xml:marshalling-transformer">
	<xsl:call-template name="generic-transformer">
	<xsl:with-param name="stereotype">Marshaller</xsl:with-param>
	</xsl:call-template>
</xsl:template>
<xsl:template match="si-xml:unmarshalling-transformer">
	<xsl:call-template name="generic-transformer">
	<xsl:with-param name="stereotype">Unmarshaller</xsl:with-param>
	</xsl:call-template>
</xsl:template>

<xsl:template name="generic-transformer">
	<xsl:param name="stereotype" />

	<xsl:variable name="id" select="@id"/>
	<xsl:variable name="inputChannel" select="@input-channel"/>
	<xsl:variable name="outputChannel" select="@output-channel"/>
	<xsl:variable name="nodeName" select="concat('xformer_', $id)"/>
	<xsl:variable name="xformer" select="$nodeName"/>
	
	<xsl:value-of select="$xformer"/> [label="&lt;&lt;<xsl:value-of select="$stereotype"/>&gt;&gt;\n<xsl:value-of select="$id"/>"];
	<xsl:if test="string-length($inputChannel) > 0">
	channel_<xsl:value-of select="$inputChannel"/> -> <xsl:value-of select="$xformer"/>;
	</xsl:if>
	
	<xsl:if test="string-length($outputChannel) > 0">
	<xsl:value-of select="$xformer"/> -> channel_<xsl:value-of select="$outputChannel"/>;
	</xsl:if>
</xsl:template>


<xsl:template match="int:bridge">
	<xsl:variable name="id" select="@id"/>
	<xsl:variable name="inputChannel" select="@input-channel"/>
	<xsl:variable name="outputChannel" select="@output-channel"/>
	<xsl:variable name="nodeName" select="concat('bridge_', $id)"/>
	<xsl:variable name="bridge" select="$nodeName"/>

	<xsl:value-of select="$bridge"/> [label="&lt;&lt;Bridge&gt;&gt;",fillcolor=black,fontcolor=white];
	
	<xsl:if test="string-length($outputChannel) > 0">
		<xsl:value-of select="$bridge"/> -> channel_<xsl:value-of select="$outputChannel"/>;
	</xsl:if>
	
	<xsl:if test="string-length($inputChannel) > 0">
		<xsl:choose>
			<xsl:when test="int:poller">
				channel_<xsl:value-of select="$inputChannel"/> -> <xsl:value-of select="$bridge"/> [label="polled"];
			</xsl:when>
			<xsl:otherwise>
				channel_<xsl:value-of select="$inputChannel"/> -> <xsl:value-of select="$bridge"/>;
			</xsl:otherwise>
		</xsl:choose>
	</xsl:if>
	
	
	
</xsl:template>



<xsl:template match="int:chain">
	<xsl:variable name="id" select="@id"/>
	<xsl:variable name="inputChannel" select="@input-channel"/>
	<xsl:variable name="outputChannel" select="@output-channel"/>
	<xsl:variable name="nodeName" select="concat('chain_', $id)"/>
	<xsl:variable name="chain" select="$nodeName"/>

	subgraph cluster_<xsl:value-of select="position()"/> {
		label="&lt;&lt;Chain&gt;&gt;\n<xsl:value-of select="$id"/>";
		fillcolor="purple";
		<xsl:value-of select="$nodeName"/> [shape=circle,size=1,label="",fillcolor="black"];
		<xsl:for-each select="int:*">
			<xsl:choose>
				
				<xsl:when test="local-name() = 'header-enricher'">
					<xsl:variable name="heNodeName" select="concat($chain,'_he_',position())" />
					<xsl:value-of select="$heNodeName"/> [label="header\nenricher",fillcolor=yellow];
				</xsl:when>

				<xsl:when test="local-name() = 'recipient-list-router'">
					<xsl:variable name="rlrNodeName" select="concat($chain,'_rlr_',position())" />
					<xsl:value-of select="$rlrNodeName"/> [label="recip list router",fillcolor=yellow];
				</xsl:when>
				
				<xsl:when test="local-name() = 'service-activator'">
					<xsl:variable name="saNodeName" select="concat($chain,'_sa_',position())" />
					
					
					<xsl:choose>
						<xsl:when test="@ref">
							<xsl:value-of select="$saNodeName"/> [fillcolor=green,label="&lt;&lt;Service Activator&gt;&gt;\n<xsl:value-of select="@ref"/>.<xsl:value-of select="@method"/>"];
						</xsl:when>
						<xsl:when test="@expression">
							<xsl:value-of select="$saNodeName"/> [fillcolor=green,label="&lt;&lt;Service Activator&gt;&gt;\n<xsl:value-of select="@expression"/>"];
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$saNodeName"/> [fillcolor=green,label="&lt;&lt;Service Activator&gt;&gt;"];
						</xsl:otherwise>
					</xsl:choose>
					
					
					
				</xsl:when>
				<xsl:when test="local-name() = 'transformer'">
					<xsl:variable name="xformerNodeName" select="concat($chain,'_xform_',position())" />
					<xsl:value-of select="$xformerNodeName"/> [label="xformer",fillcolor=orange];
				</xsl:when>
				<xsl:when test="local-name() = 'gateway'">
					<xsl:variable name="requestChannel" select="@request-channel"/>
					<xsl:variable name="gwNodeName" select="concat($chain,'_gw_',position())" />
					<xsl:value-of select="$gwNodeName"/> [label="gw",fillcolor=orange];
				</xsl:when>
			</xsl:choose>
		</xsl:for-each>
	}	
	
	<!-- have to do the 'connections' OUTSIDE the subgraph -->
	<xsl:if test="string-length($outputChannel) > 0">
		<xsl:value-of select="$chain"/> -> channel_<xsl:value-of select="$outputChannel"/>;
	</xsl:if>
	<xsl:if test="string-length($inputChannel) > 0">
		<xsl:choose>
			<xsl:when test="int:poller">
				channel_<xsl:value-of select="$inputChannel"/> -> <xsl:value-of select="$nodeName"/> [label="polled"];
			</xsl:when>
			<xsl:otherwise>
				channel_<xsl:value-of select="$inputChannel"/> -> <xsl:value-of select="$nodeName"/>;
			</xsl:otherwise>
		</xsl:choose>
	</xsl:if>
	
	<xsl:for-each select="int:*">
		<xsl:choose>
			<xsl:when test="local-name() = 'recipient-list-router'">
				<xsl:variable name="rlrNodeName" select="concat($chain,'_rlr_',position())" />
				<xsl:for-each select="int:recipient">
					<xsl:value-of select="$rlrNodeName"/> -> channel_<xsl:value-of select="@channel"/>;
				</xsl:for-each>
			</xsl:when>
			<xsl:when test="local-name() = 'gateway'">
				<xsl:variable name="requestChannel" select="@request-channel"/>
				<xsl:variable name="gwNodeName" select="concat($chain,'_gw_',position())" />
			
				<!--<xsl:value-of select="$chain"/> -> <xsl:value-of select="$gwNodeName"/>;-->
				<xsl:value-of select="$gwNodeName"/> -> <xsl:value-of select="concat('channel_',$requestChannel)"/>;
			</xsl:when>
		</xsl:choose>
	</xsl:for-each>
	
</xsl:template>
	
	
	
	
	
	
	
	
	
	
	
	
	<xsl:template name="attributes">
		<xsl:param name="prefix"/>
		<xsl:for-each select="@*">
			<xsl:choose>
				<xsl:when test="name() = 'scope'"></xsl:when>
				<xsl:when test="name() = 'class'"></xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$prefix"/> -> <xsl:value-of select="$prefix"/>_<xsl:value-of select="v:fix(name())"/>; 
		           	<xsl:value-of select="$prefix"/>_<xsl:value-of select="v:fix(name())"/> [label="<xsl:value-of select="concat(name(), ': ', ., ' ')"/>"];
		           	<xsl:value-of select="$prefix"/>_<xsl:value-of select="v:fix(name())"/> [fontcolor=blue];
				</xsl:otherwise>
			</xsl:choose>
       	</xsl:for-each>
		<xsl:for-each select="beans:constructor-arg/beans:value">
		<xsl:value-of select="$prefix"/> -> <xsl:value-of select="$prefix"/>_ctor_arg_<xsl:value-of select="position()"/>;
		<xsl:value-of select="$prefix"/>_ctor_arg_<xsl:value-of select="position()"/> [label="Arg <xsl:value-of select="."/>"];
		<xsl:value-of select="$prefix"/>_ctor_arg_<xsl:value-of select="position()"/> [fontcolor=blue];
		</xsl:for-each>		
		
	</xsl:template>
	
	
	<xsl:function name="v:simpleClassName">
		<xsl:param name="className"/>
		<xsl:variable name="x" select="tokenize($className, '\.')" />
		<xsl:value-of select="$x[last()]"/>
	</xsl:function>
	<xsl:function name="v:fix">
		<xsl:param name="in"/>
		<xsl:value-of select="translate($in,'-:','__')"/>
	</xsl:function>	
</xsl:stylesheet>
