import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { ICar } from 'app/shared/model/car.model';
import { getEntities as getCars } from 'app/entities/available-car/car.reducer';
import { getEntity, updateEntity, createEntity, reset } from './active-rent.reducer';
import { IActiveRent } from 'app/shared/model/active-rent.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import moment from "moment";

export interface IActiveRentUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ActiveRentUpdate = (props: IActiveRentUpdateProps) => {
  const [clientId, setClientId] = useState('0');
  const [carId, setCarId] = useState(props.match.params.id);
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { activeRentEntity, users, cars, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/active-rent' + props.location.search);
  };

  useEffect(() => {
    props.reset()

    props.getUsers();
    props.getCars();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...activeRentEntity,
        ...values,
      };
      entity.client = users[0]
      entity.duration = moment.duration(entity.duration).toISOString()
      props.createEntity(entity)
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="rentalApp.activeRent.home.createOrEditLabel">Create or edit a ActiveRent</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : activeRentEntity} onSubmit={saveEntity}>
              <AvGroup>
                <Label id="durationLabel" for="active-rent-duration">
                  Duration (hh:mm:ss)
                </Label>
                <AvField
                  id="active-rent-duration"
                  type="text"
                  name="duration"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="active-rent-car">Car</Label>
                <AvInput
                  id="active-rent-car"
                  type="select"
                  className="form-control"
                  name="car.id"
                  value={carId}
                  required
                >
                  {cars
                    ? cars.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.model.name}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>This field is required.</AvFeedback>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/available-car" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  users: storeState.userManagement.users,
  cars: storeState.car.entities,
  activeRentEntity: storeState.activeRent.entity,
  loading: storeState.activeRent.loading,
  updating: storeState.activeRent.updating,
  updateSuccess: storeState.activeRent.updateSuccess,
});

const mapDispatchToProps = {
  getUsers,
  getCars,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ActiveRentUpdate);
