<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:folks="http://olgak.ru/folks/v1">

    <xsl:variable name="folkFieldsToSelect">|surname|name|patronymic|</xsl:variable>
    <xsl:variable name="attributeFieldsToSkip">|author|</xsl:variable>

    <xsl:template match="/*">
        <xsl:copy>
            <xsl:apply-templates select="*">
                <xsl:sort select="@id" data-type="number"/>
            </xsl:apply-templates>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="folks:folk">
        <xsl:copy>
            <xsl:apply-templates select="@id">
                <xsl:sort select="name()"/>
            </xsl:apply-templates>
            <xsl:apply-templates select="folks:field[contains($folkFieldsToSelect, concat('|', @name, '|'))]">
                <xsl:sort select="@name"/>
            </xsl:apply-templates>
            <xsl:apply-templates select="folks:attribute">
                <xsl:sort select="@type"/>
                <xsl:sort select="@id" data-type="number"/>
            </xsl:apply-templates>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="folks:attribute">
        <xsl:copy>
            <xsl:apply-templates select="@*">
                <xsl:sort select="name()"/>
            </xsl:apply-templates>
            <xsl:apply-templates select="folks:field[not(contains($attributeFieldsToSkip, concat('|', @name, '|') )  )]">
                <xsl:sort select="@name"/>
            </xsl:apply-templates>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()">
                <xsl:sort select="@*|node()"/>
            </xsl:apply-templates>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
