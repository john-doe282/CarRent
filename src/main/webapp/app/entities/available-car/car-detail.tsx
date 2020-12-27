import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './car.reducer';
import { ICar } from 'app/shared/model/car.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICarDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CarDetail = (props: ICarDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { carEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          Car [<b>{carEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{carEntity.description}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{carEntity.status}</dd>
          <dt>
            <span id="pricePerHour">Price Per Hour</span>
          </dt>
          <dd>{carEntity.pricePerHour}</dd>
          <dt>Model</dt>
          <dd>{carEntity.model ? carEntity.model.name : ''}</dd>
          <dt>Type</dt>
          <dd>{carEntity.type ? carEntity.type.name : ''}</dd>
          <dt>Location</dt>
          <dd>{carEntity.location ? carEntity.location.district : ''}</dd>
          <dt>Owner</dt>
          <dd>{carEntity.owner ? carEntity.owner.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/available-car" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/car/${carEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ car }: IRootState) => ({
  carEntity: car.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CarDetail);
