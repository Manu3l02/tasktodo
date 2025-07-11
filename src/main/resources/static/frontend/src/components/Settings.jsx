import Sidebar from './Sidebar';
import ProfileImageUpload from './ProfileImageUpload';
import { Helmet } from "react-helmet-async";
import '../styles/Settings.css';

const Settings = () => {
  return (
	<>
	<Helmet>
	  <title>Settings - ToDoList App</title>
	</Helmet>
    <div className="settings-layout">
      <Sidebar />
      <div className="settings-content">
	    <header className='dashboard-header'>
	      <h1 className='title is-3'>Settings</h1>
	    </header>
        <ProfileImageUpload />
      </div>
    </div>
	</>
  );
};

export default Settings;