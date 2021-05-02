import {Fragment} from 'react';

import classes from './Layout.module.css';
import Header from './Header';

const layout = props => (
    <Fragment>
        <Header/>
        <main className={classes.content}>
            {props.children}
        </main>
    </Fragment>
);

export default layout;