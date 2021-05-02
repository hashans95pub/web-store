import classes from './Header.module.css';
import NavItem from './NavItem';

const header = props => (
    <header className={classes.header}>
        <nav className={classes.nav}>
            <ul>
                <NavItem link="/pricelist">Price List</NavItem>
                <NavItem link="/calculator">Calculator</NavItem>
            </ul>
        </nav>
    </header>
);

export default header;