import './footer.scss';

import React from 'react';

import { Col, Row } from 'reactstrap';

const Footer = props => (
  <div className="footer page-content">
    <Row>
      <Col md="12">
        <h5>Copyright &copy; 2020 All Rights Reserved by Some Corporation</h5>
      </Col>
    </Row>
  </div>
);

export default Footer;
