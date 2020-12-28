import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';

import { connect } from 'react-redux';
import { Row, Col, Alert } from 'reactstrap';

import { IRootState } from 'app/shared/reducers';

export type IHomeProp = StateProps;

export const Home = (props: IHomeProp) => {
  const { account } = props;

  return (
    <Row>
      <Col md="9">
        <h2>Welcome to Car Rental Service!</h2>
        <p className="lead">This is your homepage</p>
        {account && account.login ? (
          <div>
            <Alert color="success">You are logged in as user {account.login}.</Alert>
          </div>
        ) : (
          <div>
            <Alert color="warning">
              If you want to use this service, you need to be
              <Link to="/login" className="alert-link">
                {' '}
                signed in
              </Link>
            </Alert>
            <Alert color="warning">
              You do not have an account yet?&nbsp;
              <Link to="/account/register" className="alert-link">
                Register a new account
              </Link>
            </Alert>
          </div>
        )}
        <p>If you have any suggestions or complaints, please contact us:</p>

        <ul>
          <li>
            <a href="https://github.com/john-doe282/CarRent/issues" target="_blank" rel="noopener noreferrer">
              Car rental bug tracker
            </a>
          </li>
          <li>
            Send email at:
            <a href="mailto: qwerty@gmail.com">  qwerty@gmail.com</a>
          </li>
          <li>
            Call center:
            <a href="tel: (044) 456 32 24">  (044) 456 32 24</a>
          </li>
        </ul>
        <p>
        </p>
      </Col>
      <Col md="3" className="pad">
        <span className="hipster rounded" />
      </Col>
    </Row>
  );
};

const mapStateToProps = storeState => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated,
});

type StateProps = ReturnType<typeof mapStateToProps>;

export default connect(mapStateToProps)(Home);
