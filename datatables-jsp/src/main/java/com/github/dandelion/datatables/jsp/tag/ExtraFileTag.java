/*
 * [The "BSD licence"]
 * Copyright (c) 2013 Dandelion
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 3. Neither the name of Dandelion nor the names of its contributors 
 * may be used to endorse or promote products derived from this software 
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.dandelion.datatables.jsp.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.github.dandelion.core.utils.EnumUtils;
import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.asset.ExtraFile;
import com.github.dandelion.datatables.core.asset.InsertMode;
import com.github.dandelion.datatables.core.extension.feature.ExtraFileFeature;

/**
 * <p>
 * JSP tag used to insert an etra Javascript file inside the one generated by
 * Dandelion-DataTables.
 * <p>
 * Note that this tag will be processed only once, at the first iteration.
 * 
 * <p>
 * Example usage:
 * 
 * <pre>
 * &lt;datatables:table id="myTableId" data="${persons}">
 *    &lt;datatables:column title="Id" property="id" />
 *    &lt;datatables:column title="Firstname" property="firstName" />
 *    &lt;datatables:column title="LastName" property="lastName" />
 *    &lt;datatables:column title="City" property="address.town.name" />
 *    &lt;datatables:column title="Mail" property="mail" />
 *    &lt;datatables:extraFile src="/assets/js/datatables.extraFile.js" insert="BEFORESTARTDOCUMENTREADY" />
 * &lt;/datatables:table>
 * </pre>
 * 
 * @author Thibault Duchateau
 */
public class ExtraFileTag extends TagSupport {

	private static final long serialVersionUID = -287813095911386884L;

	/**
	 * Tag attributes
	 */
	private String src;
	private String insert;

	/**
	 * {@inheritDoc}
	 */
	public int doStartTag() throws JspException {

		TableTag parent = (TableTag) findAncestorWithClass(this, TableTag.class);
		if(parent != null){
			return SKIP_BODY;
		}

		throw new JspException("The tag 'extraFile' must be inside the 'table' tag.");
	}

	/**
	 * {@inheritDoc}
	 */
	public int doEndTag() throws JspException {

		AbstractTableTag parent = (AbstractTableTag) findAncestorWithClass(this, AbstractTableTag.class);

		// The tag is evaluated only once, at the first iteration
		if (parent.isFirstIteration()) {

			InsertMode mode = null;

			if (StringUtils.isNotBlank(this.insert)) {
				try {
					mode = InsertMode.valueOf(this.insert.toUpperCase().trim());
				} catch (IllegalArgumentException e) {
					StringBuilder sb = new StringBuilder();
					sb.append("'");
					sb.append(this.insert);
					sb.append("' is not a valid insert mode. Possible values are: ");
					sb.append(EnumUtils.printPossibleValuesOf(InsertMode.class));
					throw new JspException(sb.toString());
				}
			} else {
				mode = InsertMode.BEFOREALL;
			}

			parent.getTable().getTableConfiguration().addExtraFile(new ExtraFile(getRealSource(this.src), mode));
			parent.getTable().getTableConfiguration().registerExtension(new ExtraFileFeature());
		}
		return EVAL_PAGE;
	}

	/**
	 * Gets the real path corresponding to the given path.
	 * 
	 * @param virtualPath
	 *            The virtual path pointed by the src attribute.
	 * @return the corresponding real path.
	 */
	private String getRealSource(String virtualPath) {
		return pageContext.getServletContext().getRealPath(virtualPath);
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public void setInsert(String insert) {
		this.insert = insert;
	}
}