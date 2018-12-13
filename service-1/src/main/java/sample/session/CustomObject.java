package sample.session;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class CustomObject implements Serializable {
    private String test;

    public String getTest() {
        return test;
    }
    public CustomObject setTest( final String test ) {
        this.test = test;
        return this;
    }

    @Override
    public boolean equals( final Object o ) {
        return EqualsBuilder.reflectionEquals( this, o );
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode( this );
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString( this,
                ToStringStyle.SHORT_PREFIX_STYLE );
    }

}
