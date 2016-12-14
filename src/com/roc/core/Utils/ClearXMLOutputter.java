package com.roc.core.Utils;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.XMLOutputter;

import java.io.IOException;
import java.io.Writer;
import java.util.List;


/**
 * User: Lu Tingming
 * Date: 2010-11-11 20:53:54
 * Desc: XML输出器，每个属性占一行
 */
public class ClearXMLOutputter extends XMLOutputter {
    /**
     * This will handle printing of any needed <code>{@link Namespace}</code>
     * declarations.
     *
     * @param ns  <code>Namespace</code> to print definition of
     * @param out <code>Writer</code> to use.
     */
    private void printNamespace(Writer out, Namespace ns, NamespaceStack namespaces)
            throws IOException {
        String prefix = ns.getPrefix();
        String uri = ns.getURI();

        // Already printed namespace decl?
        if (uri.equals(namespaces.getURI(prefix))) {
            return;
        }

        out.write(" xmlns");
        if (!prefix.equals("")) {
            out.write(":");
            out.write(prefix);
        }
        out.write("=\"");
        out.write(escapeAttributeEntities(uri));
        out.write("\"");
        namespaces.push(ns);
    }


    // Support method to print a name without using att.getQualifiedName()
    // and thus avoiding a StringBuffer creation and memory churn
    private void printQualifiedName(Writer out, Attribute a) throws IOException {
        String prefix = a.getNamespace().getPrefix();
        if ((prefix != null) && (!prefix.equals(""))) {
            out.write(prefix);
            out.write(':');
            out.write(a.getName());
        } else {
            out.write(a.getName());
        }
    }

    /**
     * This will handle printing of a <code>{@link org.jdom.Attribute}</code> list.
     *
     * @param attributes <code>List</code> of Attribute objcts
     * @param out        <code>Writer</code> to use
     */
    protected void printAttributes(Writer out, List attributes, Element parent,
                                   NamespaceStack namespaces)
            throws IOException {

        // I do not yet handle the case where the same prefix maps to
        // two different URIs. For attributes on the same element
        // this is illegal; but as yet we don't throw an exception
        // if someone tries to do this
        // Set prefixes = new HashSet();
        for (int i = 0; i < attributes.size(); i++) {
            Attribute attribute = (Attribute) attributes.get(i);
            Namespace ns = attribute.getNamespace();
            if ((ns != Namespace.NO_NAMESPACE) &&
                    (ns != Namespace.XML_NAMESPACE)) {
                printNamespace(out, ns, namespaces);
            }

            // begin: modified by lutm at 2010-06-08
            //out.write(" ");
            out.write("\r\n\t\t");
            // end:   modified by lutm at 2010-06-08

            printQualifiedName(out, attribute);
            out.write("=");

            out.write("\"");
            out.write(escapeAttributeEntities(attribute.getValue()));
            out.write("\"");
        }
    }

}
