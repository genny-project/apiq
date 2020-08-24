
package com.cdi.crud.infra.model;

/**
 *
 * @author acrow
 */
public interface CodedEntityInterface extends CoreEntityInterface{


    public <T extends CoreEntityInterface> T getCode();


}
