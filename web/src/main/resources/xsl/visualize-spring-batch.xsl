<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:beans="http://www.springframework.org/schema/beans"
	xmlns:batch="http://www.springframework.org/schema/batch"
	xmlns:v="http://com.github.trevershick/schemas/visualize">

	<xsl:output encoding="UTF-8" indent="no" method="text" />

	<xsl:template match="/beans:beans">
		<xsl:apply-templates select="batch:*"/>
	</xsl:template>

	<xsl:template match="batch:job">
digraph {
	rankdir=LR;
	node[shape=box,style=filled];
	label="<xsl:value-of select="@id"/>";
<xsl:apply-templates select="batch:*"/>
	START [fillcolor="blue"];
	COMPLETED [fillcolor="blue"];
	{rank=same;START,<xsl:for-each select="//batch:step">,<xsl:value-of select="v:fix(@id)"/></xsl:for-each>,COMPLETED}
}
</xsl:template>
	
	<xsl:template match="batch:decision">
		<xsl:variable name="stepId">
			<xsl:value-of select="v:fix(@id)"/>
		</xsl:variable>
		<xsl:value-of select="v:fix(@id)"/> [label="&lt;&lt;decision&gt;&gt;\n<xsl:value-of select="@id"/>",shape="box"];

		<xsl:call-template name="handle_fail"/>
		<xsl:call-template name="handle_next"/>
		<xsl:apply-templates select="batch:*"/>
	</xsl:template>
	
	
	
	<xsl:template match="batch:step">
		<xsl:variable name="stepId">
			<xsl:value-of select="v:fix(@id)"/>
		</xsl:variable>
		<xsl:value-of select="v:fix(@id)"/> [label="&lt;&lt;step&gt;&gt;\n<xsl:value-of select="@id"/>"];
		<xsl:value-of select="v:fix(@id)"/> [fontcolor=white,fillcolor=blue];

		<xsl:call-template name="handle_fail"/>
		<xsl:call-template name="handle_next"/>
		<xsl:apply-templates select="batch:*"/>

		<xsl:if test="not(preceding-sibling::*)">
			 START -> <xsl:value-of select="v:fix(@id)"/>;
		</xsl:if>
		
		<xsl:if test="not(following-sibling::*)">
			<xsl:value-of select="v:fix(@id)"/> -> COMPLETED;
		</xsl:if>
		
		<xsl:for-each select="batch:listeners/batch:listener/beans:bean[@class='org.springframework.batch.core.listener.ExecutionContextPromotionListener']/beans:property[@name='keys']//beans:value">
		var_<xsl:value-of select="."/> [label="Promote '<xsl:value-of select="."/>'",fontcolor="#339933"];
		<xsl:value-of select="$stepId"/> -> var_<xsl:value-of select="."/>;
		</xsl:for-each>
		
	</xsl:template>
	

	<xsl:template name="handle_next">
		<xsl:if test="@next">
			<xsl:value-of select="v:fix(@id)"/> -> <xsl:value-of select="v:fix(@next)"/>;
		</xsl:if>
	</xsl:template>
	<xsl:template name="handle_fail">
		<xsl:if test="@fail">
			<xsl:value-of select="v:fix(@id)"/> -> <xsl:value-of select="v:fix(@fail)"/> [color="red"];
		</xsl:if>
	</xsl:template>
	
	<!-- if the fail is just FAILED/FAILED then that's default behavior
	and let's not put that in jsut yet -->
	<xsl:template match="batch:fail[@on != 'FAILED' and @exit-code != 'FAILED']">
		<xsl:value-of select="v:fix(../@id)"/>
		<xsl:text> -> </xsl:text>
		<xsl:value-of select="@exit-code"/>
		<xsl:text> [color="red",label="</xsl:text>
		<xsl:value-of select="@on"/>
		<xsl:text>"];</xsl:text>
	</xsl:template>
	
	<xsl:template match="batch:next">
		<xsl:value-of select="../@id"/>
		<xsl:text> -> </xsl:text>
		<xsl:value-of select="@to"/>
		<xsl:text>[label="</xsl:text>
		<xsl:value-of select="@on"/>
		<xsl:text>"];</xsl:text>
	</xsl:template>
	
	<xsl:template match="batch:end">
		<xsl:value-of select="../@id"/>
		<xsl:text> -> COMPLETED</xsl:text>
		<xsl:text>[label="</xsl:text>
		<xsl:value-of select="@on"/>
		<xsl:text>"];</xsl:text>
	</xsl:template>

	<xsl:template match="batch:tasklet">
		<xsl:variable name="stepId" select="v:fix(../@id)"/>
		
		<xsl:apply-templates select="batch:*"/>
		
		<xsl:for-each select="beans:bean">
			<xsl:variable name="clazz" select="v:simpleClassName(@class)"/>
			<xsl:variable name="node" select="concat($stepId,'_',$clazz)"/>

			<xsl:value-of select="$stepId"/> -> <xsl:value-of select="$node"/>; 
			<xsl:value-of select="$node"/> [label="Tasklet\n<xsl:value-of select="$clazz"/>"];
			<xsl:value-of select="$node"/> [fillcolor=yellow];
			
		
			<xsl:call-template name="attributes">
				<xsl:with-param name="prefix" select="$node"/>
			</xsl:call-template>
		
		</xsl:for-each>

	</xsl:template>







	<xsl:template match="batch:chunk">
		
		<xsl:variable name="stepId" select="v:fix(../../@id)"/>
		
		<xsl:value-of select="$stepId"/> -> <xsl:value-of select="$stepId"/>_chunk;
		<xsl:value-of select="$stepId"/>_chunk [label="Chunk Processing"];
		
		<xsl:call-template name="attributes">
			<xsl:with-param name="prefix" select="concat($stepId,'_chunk')"/>
		</xsl:call-template>

		<xsl:if test="batch:reader">
			<xsl:variable name="clazz" select="v:simpleClassName(batch:reader/beans:bean/@class)"/>
			<xsl:variable name="node" select="concat($stepId,'_',$clazz)"/>

			<xsl:value-of select="concat($stepId,'_chunk')"/> -> <xsl:value-of select="$node"/>;
			<xsl:value-of select="$node"/> [label="Reader\n<xsl:value-of select="$clazz"/>"];
			<xsl:value-of select="$node"/> [fillcolor=green];
			
			<xsl:for-each select="batch:reader/beans:bean">
				<xsl:call-template name="attributes">
					<xsl:with-param name="prefix" select="concat($stepId,'_',$clazz)"/>
				</xsl:call-template>
			</xsl:for-each>
		</xsl:if>
		<xsl:if test="batch:processor">
			<xsl:variable name="clazz" select="v:simpleClassName(batch:processor/beans:bean/@class)"/>
			<xsl:variable name="node" select="concat($stepId,'_',$clazz)"/>
			
			<xsl:value-of select="concat($stepId,'_chunk')"/> -> <xsl:value-of select="$node"/>;
			<xsl:value-of select="$node"/> [label="Processor\n<xsl:value-of select="$clazz"/>"];
			<xsl:value-of select="$node"/> [fillcolor=green];

			<xsl:for-each select="batch:processor/beans:bean">
				<xsl:call-template name="attributes">
					<xsl:with-param name="prefix" select="concat($stepId,'_',$clazz)"/>
				</xsl:call-template>
			</xsl:for-each>
		</xsl:if>
		<xsl:if test="batch:writer">
			<xsl:variable name="clazz" select="v:simpleClassName(batch:writer/beans:bean/@class)"/>
			<xsl:variable name="node" select="concat($stepId,'_',$clazz)"/>
			
			<xsl:value-of select="concat($stepId,'_chunk')"/> -> <xsl:value-of select="$node"/>;
			<xsl:value-of select="$node"/> [label="Writer\n<xsl:value-of select="$clazz"/>"];
			<xsl:value-of select="$node"/> [fillcolor=green];
			

			<xsl:for-each select="batch:writer/beans:bean">
				<xsl:call-template name="attributes">
					<xsl:with-param name="prefix" select="concat($stepId,'_',$clazz)"/>
				</xsl:call-template>
			</xsl:for-each>
		</xsl:if>


	</xsl:template>

	<xsl:template match="batch:listeners">
	</xsl:template>
	
	<xsl:template match="batch:description"></xsl:template>
		
	
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
