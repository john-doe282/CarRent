import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './active-rent.reducer';
import { IActiveRent } from 'app/shared/model/active-rent.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import moment from "moment";

export interface IActiveRentDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ActiveRentDetail = (props: IActiveRentDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { activeRentEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          ActiveRent [<b>{activeRentEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="duration">Duration</span>
          </dt>
          <dd>{moment.duration(activeRentEntity.duration).asMinutes()} min</dd>
          <dt>Client</dt>
          <dd>{activeRentEntity.client ? activeRentEntity.client.login : ''}</dd>
          <dt>Car</dt>
          <dd>{activeRentEntity.car ? activeRentEntity.car.pricePerHour : ''}</dd>
        </dl>
        <Button tag={Link} to="/active-rent" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/active-rent/${activeRentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ activeRent }: IRootState) => ({
  activeRentEntity: activeRent.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ActiveRentDetail);
