<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:j.0="http://fedora.info/definitions/v4/repository#" xmlns:j.1="http://www.openarchives.org/OAI/2.0/" xmlns:j.2="http://www.w3.org/ns/ldp#">
    <xsl:output method="xml"/>
    <xsl:template match="/rdf:RDF">
        <OAI-PMH>
            <responseDate/>
            <request verb="ListRecords" metadataPrefix="oai_dc" set="gmd"/>
            <ListRecords>
                <xsl:for-each select="child::*">
                    <record>
                        <header>
                            <identifier><xsl:value-of select="./@rdf:about"/></identifier>
                            <datestamp><xsl:value-of select="j.1:datestamp"/></datestamp>
                            <setSpec>gmd</setSpec>
                        </header>
                        <metadata>
                            <oai_dc:dc xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd">
                                <xsl:if test="dc:title">
                                    <dc:title><xsl:value-of select="normalize-space(dc:title)"/></dc:title>
                                </xsl:if>
                                <xsl:if test="dc:creator">
                                    <dc:creator><xsl:value-of select="normalize-space(dc:creator)"/></dc:creator>
                                </xsl:if>
                                <xsl:if test="dc:subject">
                                    <dc:subject><xsl:value-of select="normalize-space(dc:subject)"/></dc:subject>
                                </xsl:if>
                                <xsl:if test="dc:description">
                                    <xsl:for-each select="dc:description">
                                        <dc:description><xsl:value-of select="normalize-space(.)"/></dc:description>
                                    </xsl:for-each>
                                </xsl:if>
                                <xsl:if test="dc:date">
                                    <dc:date><xsl:value-of select="normalize-space(dc:date)"/></dc:date>
                                </xsl:if>
                                <xsl:if test="dc:type">
                                    <dc:type><xsl:value-of select="normalize-space(dc:type)"/></dc:type>
                                </xsl:if>
                                <xsl:if test="dc:identifier">
                                    <dc:identifier><xsl:value-of select="normalize-space(dc:identifier)"/></dc:identifier>
                                </xsl:if>
                                <xsl:if test="dc:language">
                                    <dc:language><xsl:value-of select="normalize-space(dc:language)"/></dc:language>
                                </xsl:if>
                                <xsl:if test="dc:coverage">
                                    <dc:coverage><xsl:value-of select="normalize-space(dc:coverage)"/></dc:coverage>
                                </xsl:if>
                            </oai_dc:dc>
                        </metadata>
                    </record>
                </xsl:for-each>
            </ListRecords>
        </OAI-PMH>
    </xsl:template>
</xsl:stylesheet>
