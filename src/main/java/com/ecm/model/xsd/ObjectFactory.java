//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2018.04.10 时间 11:51:57 PM CST
//


package com.ecm.model.xsd;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.ecm.model.xsd package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _EcmRelationsRelation_QNAME = new QName("http://www.runoob.com", "relation");
    private final static QName _EvidenceType_QNAME = new QName("http://www.runoob.com", "type");
    private final static QName _EvidenceContent_QNAME = new QName("http://www.runoob.com", "content");
    private final static QName _EvidenceName_QNAME = new QName("http://www.runoob.com", "name");
    private final static QName _EvidenceHeads_QNAME = new QName("http://www.runoob.com", "heads");
    private final static QName _EvidenceCommitter_QNAME = new QName("http://www.runoob.com", "committer");
    private final static QName _EvidenceReason_QNAME = new QName("http://www.runoob.com", "reason");
    private final static QName _EvidenceTrust_QNAME = new QName("http://www.runoob.com", "trust");
    private final static QName _FactJointsJoint_QNAME = new QName("http://www.runoob.com", "joint");
    private final static QName _ArrowHead_QNAME = new QName("http://www.runoob.com", "head");
    private final static QName _EcmRelationsRelationArrows_QNAME = new QName("http://www.runoob.com", "arrows");
    private final static QName _EcmFactsFact_QNAME = new QName("http://www.runoob.com", "fact");
    private final static QName _EcmRelationsRelationArrowsArrow_QNAME = new QName("http://www.runoob.com", "arrow");
    private final static QName _EcmEvidencesEvidence_QNAME = new QName("http://www.runoob.com", "evidence");
    private final static QName _FactJoints_QNAME = new QName("http://www.runoob.com", "joints");
    private final static QName _EcmFacts_QNAME = new QName("http://www.runoob.com", "facts");
    private final static QName _EcmEvidences_QNAME = new QName("http://www.runoob.com", "evidences");
    private final static QName _EcmRelations_QNAME = new QName("http://www.runoob.com", "relations");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.ecm.model.xsd
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Ecm }
     *
     */
    public Ecm createEcm() {
        return new Ecm();
    }

    /**
     * Create an instance of {@link Evidence }
     *
     */
    public Evidence createEvidence() {
        return new Evidence();
    }

    /**
     * Create an instance of {@link Fact }
     *
     */
    public Fact createFact() {
        return new Fact();
    }

    /**
     * Create an instance of {@link Ecm.Relations }
     *
     */
    public Ecm.Relations createEcmRelations() {
        return new Ecm.Relations();
    }

    /**
     * Create an instance of {@link Ecm.Relations.Relation }
     *
     */
    public Ecm.Relations.Relation createEcmRelationsRelation() {
        return new Ecm.Relations.Relation();
    }

    /**
     * Create an instance of {@link Ecm.Evidences }
     *
     */
    public Ecm.Evidences createEcmEvidences() {
        return new Ecm.Evidences();
    }

    /**
     * Create an instance of {@link Ecm.Facts }
     *
     */
    public Ecm.Facts createEcmFacts() {
        return new Ecm.Facts();
    }

    /**
     * Create an instance of {@link Head }
     *
     */
    public Head createHead() {
        return new Head();
    }

    /**
     * Create an instance of {@link Joint }
     *
     */
    public Joint createJoint() {
        return new Joint();
    }

    /**
     * Create an instance of {@link Arrow }
     *
     */
    public Arrow createArrow() {
        return new Arrow();
    }

    /**
     * Create an instance of {@link Evidence.Heads }
     *
     */
    public Evidence.Heads createEvidenceHeads() {
        return new Evidence.Heads();
    }

    /**
     * Create an instance of {@link Fact.Joints }
     *
     */
    public Fact.Joints createFactJoints() {
        return new Fact.Joints();
    }

    /**
     * Create an instance of {@link Ecm.Relations.Relation.Arrows }
     *
     */
    public Ecm.Relations.Relation.Arrows createEcmRelationsRelationArrows() {
        return new Ecm.Relations.Relation.Arrows();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Ecm.Relations.Relation }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.runoob.com", name = "relation", scope = Ecm.Relations.class)
    public JAXBElement<Ecm.Relations.Relation> createEcmRelationsRelation(Ecm.Relations.Relation value) {
        return new JAXBElement<Ecm.Relations.Relation>(_EcmRelationsRelation_QNAME, Ecm.Relations.Relation.class, Ecm.Relations.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.runoob.com", name = "type", scope = Evidence.class)
    public JAXBElement<String> createEvidenceType(String value) {
        return new JAXBElement<String>(_EvidenceType_QNAME, String.class, Evidence.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.runoob.com", name = "content", scope = Evidence.class)
    public JAXBElement<String> createEvidenceContent(String value) {
        return new JAXBElement<String>(_EvidenceContent_QNAME, String.class, Evidence.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.runoob.com", name = "name", scope = Evidence.class)
    public JAXBElement<String> createEvidenceName(String value) {
        return new JAXBElement<String>(_EvidenceName_QNAME, String.class, Evidence.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Evidence.Heads }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.runoob.com", name = "heads", scope = Evidence.class)
    public JAXBElement<Evidence.Heads> createEvidenceHeads(Evidence.Heads value) {
        return new JAXBElement<Evidence.Heads>(_EvidenceHeads_QNAME, Evidence.Heads.class, Evidence.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.runoob.com", name = "committer", scope = Evidence.class)
    public JAXBElement<String> createEvidenceCommitter(String value) {
        return new JAXBElement<String>(_EvidenceCommitter_QNAME, String.class, Evidence.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.runoob.com", name = "reason", scope = Evidence.class)
    public JAXBElement<String> createEvidenceReason(String value) {
        return new JAXBElement<String>(_EvidenceReason_QNAME, String.class, Evidence.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.runoob.com", name = "trust", scope = Evidence.class)
    public JAXBElement<String> createEvidenceTrust(String value) {
        return new JAXBElement<String>(_EvidenceTrust_QNAME, String.class, Evidence.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Joint }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.runoob.com", name = "joint", scope = Fact.Joints.class)
    public JAXBElement<Joint> createFactJointsJoint(Joint value) {
        return new JAXBElement<Joint>(_FactJointsJoint_QNAME, Joint.class, Fact.Joints.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.runoob.com", name = "content", scope = Arrow.class)
    public JAXBElement<String> createArrowContent(String value) {
        return new JAXBElement<String>(_EvidenceContent_QNAME, String.class, Arrow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.runoob.com", name = "name", scope = Arrow.class)
    public JAXBElement<String> createArrowName(String value) {
        return new JAXBElement<String>(_EvidenceName_QNAME, String.class, Arrow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Head }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.runoob.com", name = "head", scope = Arrow.class)
    public JAXBElement<Head> createArrowHead(Head value) {
        return new JAXBElement<Head>(_ArrowHead_QNAME, Head.class, Arrow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.runoob.com", name = "content", scope = Joint.class)
    public JAXBElement<String> createJointContent(String value) {
        return new JAXBElement<String>(_EvidenceContent_QNAME, String.class, Joint.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.runoob.com", name = "name", scope = Joint.class)
    public JAXBElement<String> createJointName(String value) {
        return new JAXBElement<String>(_EvidenceName_QNAME, String.class, Joint.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Ecm.Relations.Relation.Arrows }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.runoob.com", name = "arrows", scope = Ecm.Relations.Relation.class)
    public JAXBElement<Ecm.Relations.Relation.Arrows> createEcmRelationsRelationArrows(Ecm.Relations.Relation.Arrows value) {
        return new JAXBElement<Ecm.Relations.Relation.Arrows>(_EcmRelationsRelationArrows_QNAME, Ecm.Relations.Relation.Arrows.class, Ecm.Relations.Relation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Joint }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.runoob.com", name = "joint", scope = Ecm.Relations.Relation.class)
    public JAXBElement<Joint> createEcmRelationsRelationJoint(Joint value) {
        return new JAXBElement<Joint>(_FactJointsJoint_QNAME, Joint.class, Ecm.Relations.Relation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Fact }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.runoob.com", name = "fact", scope = Ecm.Facts.class)
    public JAXBElement<Fact> createEcmFactsFact(Fact value) {
        return new JAXBElement<Fact>(_EcmFactsFact_QNAME, Fact.class, Ecm.Facts.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Head }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.runoob.com", name = "head", scope = Evidence.Heads.class)
    public JAXBElement<Head> createEvidenceHeadsHead(Head value) {
        return new JAXBElement<Head>(_ArrowHead_QNAME, Head.class, Evidence.Heads.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Arrow }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.runoob.com", name = "arrow", scope = Ecm.Relations.Relation.Arrows.class)
    public JAXBElement<Arrow> createEcmRelationsRelationArrowsArrow(Arrow value) {
        return new JAXBElement<Arrow>(_EcmRelationsRelationArrowsArrow_QNAME, Arrow.class, Ecm.Relations.Relation.Arrows.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Evidence }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.runoob.com", name = "evidence", scope = Ecm.Evidences.class)
    public JAXBElement<Evidence> createEcmEvidencesEvidence(Evidence value) {
        return new JAXBElement<Evidence>(_EcmEvidencesEvidence_QNAME, Evidence.class, Ecm.Evidences.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.runoob.com", name = "type", scope = Fact.class)
    public JAXBElement<String> createFactType(String value) {
        return new JAXBElement<String>(_EvidenceType_QNAME, String.class, Fact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.runoob.com", name = "content", scope = Fact.class)
    public JAXBElement<String> createFactContent(String value) {
        return new JAXBElement<String>(_EvidenceContent_QNAME, String.class, Fact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.runoob.com", name = "name", scope = Fact.class)
    public JAXBElement<String> createFactName(String value) {
        return new JAXBElement<String>(_EvidenceName_QNAME, String.class, Fact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Fact.Joints }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.runoob.com", name = "joints", scope = Fact.class)
    public JAXBElement<Fact.Joints> createFactJoints(Fact.Joints value) {
        return new JAXBElement<Fact.Joints>(_FactJoints_QNAME, Fact.Joints.class, Fact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.runoob.com", name = "content", scope = Head.class)
    public JAXBElement<String> createHeadContent(String value) {
        return new JAXBElement<String>(_EvidenceContent_QNAME, String.class, Head.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.runoob.com", name = "name", scope = Head.class)
    public JAXBElement<String> createHeadName(String value) {
        return new JAXBElement<String>(_EvidenceName_QNAME, String.class, Head.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Ecm.Facts }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.runoob.com", name = "facts", scope = Ecm.class)
    public JAXBElement<Ecm.Facts> createEcmFacts(Ecm.Facts value) {
        return new JAXBElement<Ecm.Facts>(_EcmFacts_QNAME, Ecm.Facts.class, Ecm.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Ecm.Evidences }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.runoob.com", name = "evidences", scope = Ecm.class)
    public JAXBElement<Ecm.Evidences> createEcmEvidences(Ecm.Evidences value) {
        return new JAXBElement<Ecm.Evidences>(_EcmEvidences_QNAME, Ecm.Evidences.class, Ecm.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Ecm.Relations }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.runoob.com", name = "relations", scope = Ecm.class)
    public JAXBElement<Ecm.Relations> createEcmRelations(Ecm.Relations value) {
        return new JAXBElement<Ecm.Relations>(_EcmRelations_QNAME, Ecm.Relations.class, Ecm.class, value);
    }

}
