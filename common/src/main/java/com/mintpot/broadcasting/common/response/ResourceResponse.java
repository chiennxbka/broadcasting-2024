package com.mintpot.broadcasting.common.response;

import com.mintpot.broadcasting.common.enums.CategoryFile;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResourceResponse implements Comparable<ResourceResponse> {

    private String resourcePath;

    private CategoryFile fileType;

    private Long fileSize;

    private Integer timeNavigate;

    @Override
    public int compareTo(ResourceResponse response) {
        return this.fileSize.compareTo(response.getFileSize());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (fileSize ^ (fileSize >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ResourceResponse other = (ResourceResponse) obj;
        return fileSize.equals(other.fileSize);
    }
}
