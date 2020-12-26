import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from './menu-components';

const AdminEntityItems = (
  <>
    <MenuItem icon="asterisk" to="/location">
      Location
    </MenuItem>
    <MenuItem icon="asterisk" to="/model">
      Model
    </MenuItem>
    <MenuItem icon="asterisk" to="/type">
      Type
    </MenuItem>
  </>
)

export const EntitiesMenu = props => (
  <NavDropdown icon="th-list" name="Cars" id="entity-menu" style={{ maxHeight: '80vh', overflow: 'auto' }}>

    {props.isAuthenticated && props.isAdmin && AdminEntityItems}
    <MenuItem icon="asterisk" to="/car">
      Your Cars
    </MenuItem>
    <MenuItem icon="asterisk" to="/available-car">
      Available Cars
    </MenuItem>
  </NavDropdown>
);
