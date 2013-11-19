package com.github.dandelion.datatables.core.extension.feature;

import com.github.dandelion.datatables.core.asset.JsResource;
import com.github.dandelion.datatables.core.asset.Parameter;
import com.github.dandelion.datatables.core.asset.ResourceType;
import com.github.dandelion.datatables.core.constants.DTConstants;
import com.github.dandelion.datatables.core.extension.AbstractExtension;
import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * 
 * @see http://www.datatables.net/plug-ins/pagination
 */
public class PaginationTypeBootstrapFullNumbersFeature extends AbstractExtension {

	@Override
	public String getName() {
		return "PaginationTypeBootstrapFullNumbers";
	}

	@Override
	public void setup(HtmlTable table) {
		addJsResource(new JsResource(ResourceType.FEATURE, "PaginationTypeBootstrapFullNumbers", "datatables/features/paginationType/bootstrap_full_numbers.js"));
		addParameter(new Parameter(DTConstants.DT_PAGINATION_TYPE, "bootstrap_full_numbers", Parameter.Mode.OVERRIDE));
	}
}
