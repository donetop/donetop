package com.donetop.enums.file;

import java.security.InvalidParameterException;

public enum Extension {

	JPEG,
	JPG,
	PNG,
	TXT,
	PDF,
	XLSX,
	UNKNOWN
	;

	public static Extension from(final String fileNameWithExtension) {
		if (!fileNameWithExtension.contains(".")) throw new InvalidParameterException("Filename must contain one dot character.");
		final String extension = fileNameWithExtension.substring(fileNameWithExtension.lastIndexOf(".") + 1);
		for (final Extension ext : values()) {
			if (ext.toString().toLowerCase().equals(extension)) return ext;
		}
		return Extension.UNKNOWN;
	}

}
