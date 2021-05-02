import {NavLink} from 'react-router-dom';

import classes from './NavItem.module.css';

const navItem = props => (
    <li className={classes.navitem}>
        <NavLink
            to={props.link}
            activeClassName={classes.active}
            exact={props.exact}>{props.children}</NavLink>
    </li>
);

export default navItem;