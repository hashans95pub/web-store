import React, {Fragment} from 'react';

import Modal from '../commons/Modal';
import useHttpErrorHandler from '../../hooks/http-error-handler';

const withErrorHandler = (WrappedComponent, axios) => {
    return props => {
        const [error, removeErrorHandler] = useHttpErrorHandler(axios);

        return (
            <Fragment>
                <Modal onClose={removeErrorHandler}>
                    {error ? error.message : null}
                </Modal>
                <WrappedComponent {...props} />
            </Fragment>
        );
    }
}

export default withErrorHandler;