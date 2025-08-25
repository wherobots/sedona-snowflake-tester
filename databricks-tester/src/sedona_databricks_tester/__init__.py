"""
Sedona Databricks Tester

An automated testing framework for Apache Sedona on Databricks.
"""

from pathlib import Path

__version__ = "0.1.0"
__author__ = "Your Name"
__email__ = "your.email@example.com"

# Project root directory (parent of src/)
PROJECT_ROOT = Path(__file__).parent.parent.parent

from .databricks_manager import DatabricksManager  # noqa: E402

__all__ = [
    "DatabricksManager",
    "PROJECT_ROOT",
]
