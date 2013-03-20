package com.tsystems.javaschool.webmailsystem.converter;

import com.tsystems.javaschool.webmailsystem.dto.FolderDTO;
import org.primefaces.model.DefaultTreeNode;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.swing.tree.TreeNode;

/**
 *
 */
@FacesConverter("folderConverter")
public class FolderDTOConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		String[] v = value.split(" ");
		return new FolderDTO(Long.parseLong(v[0]),v[1]);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return ((FolderDTO)value).getId() + " " + ((FolderDTO)value).getFolderName();
	}
}
